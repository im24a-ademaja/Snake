const BASE_URL = "/game";

const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const gridSize = 16;
const tileSize = canvas.width / gridSize;

let isRunning = false;
let gameOverShown = false;

async function startGame() {
    try {
        await fetch(BASE_URL + "/start", {
            method: "POST"
        });

        isRunning = true;
        gameOverShown = false;

    } catch (err) {
        console.warn("startGame failed:", err);
    }
}

setInterval(async () => {
    if (!isRunning) return;

    try {
        const response = await fetch(BASE_URL + "/state");

        if (!response.ok) return;

        const state = await response.json();

        if (!state || !state.snake || state.snake.length === 0) return;

        draw(state);

        if (state.gameOver && !gameOverShown) {
            gameOverShown = true;
            isRunning = false;

            showGameOver(state.score);
        }

    } catch (err) {
        console.warn("fetch error:", err);
    }
}, 200);

function draw(state) {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    document.getElementById("score").innerText =
        "Score: " + state.score;

    ctx.strokeStyle = "#ddd";
    for (let x = 0; x < gridSize; x++) {
        for (let y = 0; y < gridSize; y++) {
            ctx.strokeRect(
                x * tileSize,
                y * tileSize,
                tileSize,
                tileSize
            );
        }
    }

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

document.addEventListener("keydown", (e) => {
    let direction;

    if (e.key === "ArrowUp") direction = "UP";
    if (e.key === "ArrowDown") direction = "DOWN";
    if (e.key === "ArrowLeft") direction = "LEFT";
    if (e.key === "ArrowRight") direction = "RIGHT";

    if (direction) {
        fetch(BASE_URL + "/direction?direction=" + direction, {
            method: "POST"
        }).catch(err => console.warn("direction error:", err));
    }
});

async function saveScore(score) {
    const name = document.getElementById("playerName").value;

    await fetch(`/score/save?name=${name}&points=${score}`, {
        method: "POST"
    });

    loadLeaderboard();
}

async function loadLeaderboard() {
    const res = await fetch("/score/top");
    const scores = await res.json();

    const board = document.getElementById("leaderboard");

    board.innerHTML = "<h3>Top Scores</h3>";

    scores.forEach(s => {
        board.innerHTML += `<p>${s.name}: ${s.points}</p>`;
    });
}

function showGameOver(score) {
    const overlay = document.createElement("div");

    overlay.id = "gameOverScreen";
    overlay.innerHTML = `
        <h1>GAME OVER</h1>
        <p>Score: ${score}</p>
        <input id="playerName" placeholder="Username" />
        <button onclick="saveScore(${score})">Save Score</button>
        <button onclick="restartGame()">Start</button>
        <div id="leaderboard"></div>
    `;

    overlay.style.position = "absolute";
    overlay.style.top = "50%";
    overlay.style.left = "50%";
    overlay.style.transform = "translate(-50%, -50%)";
    overlay.style.background = "dodgerblue";
    overlay.style.padding = "20px";
    overlay.style.border = "2px solid white";
    overlay.style.textAlign = "center";

    document.body.appendChild(overlay);
}

function restartGame() {
    const overlay = document.getElementById("gameOverScreen");
    if (overlay) overlay.remove();

    startGame();
}
