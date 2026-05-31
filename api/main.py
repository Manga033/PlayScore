from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

app = FastAPI()


class GameCreate(BaseModel):
    name: str
    type: str
    date: str


class GameUpdate(BaseModel):
    name: Optional[str] = None
    type: Optional[str] = None
    date: Optional[str] = None


games = [
    {
        "id": 1,
        "name": "Chess Match",
        "type": "Board Game",
        "date": "31 May, 2026"
    },
    {
        "id": 2,
        "name": "Football Match",
        "type": "Sports",
        "date": "31 May, 2026"
    }
]

next_id = 3


@app.get("/games/")
def get_games():
    return games


@app.get("/games/{game_id}")
def get_game(game_id: int):
    for game in games:
        if game["id"] == game_id:
            return game
    raise HTTPException(status_code=404, detail="Game not found")


@app.post("/games/")
def create_game(game: GameCreate):
    global next_id
    new_game = {
        "id": next_id,
        "name": game.name,
        "type": game.type,
        "date": game.date
    }
    next_id += 1
    games.append(new_game)
    return new_game


@app.put("/games/{game_id}")
def update_game(game_id: int, game_update: GameUpdate):
    for game in games:
        if game["id"] == game_id:
            if game_update.name is not None:
                game["name"] = game_update.name
            if game_update.type is not None:
                game["type"] = game_update.type
            if game_update.date is not None:
                game["date"] = game_update.date
            return game
    raise HTTPException(status_code=404, detail="Game not found")


@app.delete("/games/{game_id}")
def delete_game(game_id: int):
    for game in games:
        if game["id"] == game_id:
            games.remove(game)
            return {"message": "Game deleted"}
    raise HTTPException(status_code=404, detail="Game not found")
