package com.example.playscore.model.repository.cloud

import com.example.playscore.model.domain.Game
import com.example.playscore.model.domain.Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class CloudGameRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : CloudGameRepository {

    private val gamesCollection = firestore.collection("games")

    override fun observeCloudGames(): Flow<List<Game>> {
        val currentUserId = firebaseAuth.currentUser?.uid.orEmpty()
        if (currentUserId.isBlank()) {
            return flowOf(emptyList())
        }

        return callbackFlow {
            val query = gamesCollection.whereEqualTo("userId", currentUserId)

            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val games = snapshot?.documents?.map { document ->
                    val players = (document.get("players") as? List<*>)?.mapNotNull { value ->
                        val player = value as? Map<*, *> ?: return@mapNotNull null
                        Player(
                            id = 0,
                            name = player["name"] as? String ?: "",
                            score = (player["score"] as? Long)?.toInt() ?: 0
                        )
                    }.orEmpty()

                    Game(
                        id = document.getLong("localId")?.toInt() ?: 0,
                        name = document.getString("name") ?: "",
                        type = document.getString("type") ?: "",
                        players = players,
                        date = document.getString("date") ?: ""
                    )
                }.orEmpty()

                trySend(games)
            }

            awaitClose { listener.remove() }
        }
    }

    override suspend fun addGame(
        localId: Int,
        name: String,
        type: String,
        players: List<Player>,
        date: String
    ) {
        val currentUserId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("Login is required for cloud sync.")

        val gameData = hashMapOf(
            "userId" to currentUserId,
            "localId" to localId,
            "name" to name,
            "type" to type,
            "date" to date,
            "players" to players.map { player ->
                hashMapOf(
                    "name" to player.name,
                    "score" to player.score
                )
            }
        )

        gamesCollection.add(gameData).await()
    }

}
