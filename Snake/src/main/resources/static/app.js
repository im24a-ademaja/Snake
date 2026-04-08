const BASE_URL = "/game";

const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const gridSize = 16;
const tileSize = canvas.width / gridSize;

let socket;
let gameOverShown = false;

async function startGame() {
    try {
        await fetch(BASE_URL + "/start", {
            method: "POST"
        });

        connectWebSocket();
        gameOverShown = false;

    } catch (err) {
        console.warn("startGame failed:", err);
    }
}

function connectWebSocket() {
    socket = new WebSocket("ws://localhost:8080/ws/game");

    socket.onopen = () => {
        console.log("WebSocket connected");
    };

    socket.onmessage = (event) => {
        const msg = JSON.parse(event.data);

        if (msg.type === "GAME") {
            draw(msg.data);

            if (msg.data.gameOver && !gameOverShown) {
                gameOverShown = true;
                showGameOver(msg.data.score);
            }
        }

        if (msg.type === "LEADERBOARD") {
            renderLeaderboard(msg.data);
        }
    };

    socket.onclose = () => {
        console.log("WebSocket disconnected");
    };

    socket.onerror = (err) => {
        console.error("WebSocket error:", err);
    };
}

function draw(state) {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    document.getElementById("score").innerText =
        "Score: " + state.score;

    state.snake.forEach((part, index) => {
        ctx.fillStyle = index === 0 ? "darkgreen" : "green";

        ctx.fillRect(
            part.x * tileSize,
            part.y * tileSize,
            tileSize,
            tileSize
        );
    });

    ctx.fillStyle = "red";
    ctx.fillRect(
        state.apple.x * tileSize,
        state.apple.y * tileSize,
        tileSize,
        tileSize
    );
}

function showGameOver(score) {
    const overlay = document.createElement("div");

    overlay.innerHTML = `
        <div style="
            position:fixed;
            top:50%;
            left:50%;
            transform:translate(-50%, -50%);
            background:coral;
            padding:20px;
            border:2px solid white;
            text-align:center;
            z-index:999;
        ">
            <h1>GAME OVER</h1>
            <p>Score: ${score}</p>

            <input id="playerName" placeholder="Your Name"
            style="background:coral; border:2px solid white;"/>

            <br/><br/>

            <button onclick="saveScore(${score})"
            style="background:coral; color:white; padding:10px; border:2px solid white; border-radius:5px; cursor: pointer;">
            Save Score
            </button>

            <button onclick="restartGame()"
            style="background:coral; color:white; padding:10px; border:2px solid white; border-radius:5px; cursor: pointer;">
            Restart
            </button>

            <div id="leaderboard"></div>
        </div>
    `;

    document.body.appendChild(overlay);
}

async function saveScore(score) {
    const name = document.getElementById("playerName").value;

    if (!name) {
        alert("Bitte Name eingeben!");
        return;
    }

    await fetch(`/score/save?name=${name}&points=${score}`, {
        method: "POST"
    });
}

function renderLeaderboard(scores) {
    const board = document.getElementById("leaderboard");

    if (!board) return;

    board.innerHTML = "<h3>Top Scores</h3>";

    scores.forEach((s, index) => {
        board.innerHTML += `<p>${index + 1}. ${s.name}: ${s.points}</p>`;
    });
}

function restartGame() {
    location.reload();
}

document.addEventListener("keydown", (e) => {
    let direction;

    if (e.key === "ArrowUp") direction = "UP";
    if (e.key === "ArrowDown") direction = "DOWN";
    if (e.key === "ArrowLeft") direction = "LEFT";
    if (e.key === "ArrowRight") direction = "RIGHT";

    if (direction) {
        fetch(BASE_URL + "/direction?direction=" + direction, {
            method: "POST"
        });
    }
});

window.onload = () => {
    startGame();
};