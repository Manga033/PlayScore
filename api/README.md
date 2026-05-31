# PlayScore API

This is a small local REST API for the PlayScore Android app.

Run it from this folder:

```powershell
pip install -r requirements.txt
uvicorn main:app --reload
```

Open Swagger documentation:

```text
http://127.0.0.1:8000/docs
```

Android emulator base URL:

```text
http://10.0.2.2:8000/
```

Available endpoints:

```text
GET     /games/
GET     /games/{game_id}
POST    /games/
PUT     /games/{game_id}
DELETE  /games/{game_id}
```
