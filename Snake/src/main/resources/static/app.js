const BASE_URL = "/game";

const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const gridSize = 16;
const tileSize = canvas.width / gridSize;

async function startGame() {
    await fetch(BASE_URL + "/start", {
        method: "POST"
    });
}

setInterval(async () => {
    const response = await fetch(BASE_URL + "/state");
    const state = await response.json();

    draw(state);
}, 200);

function draw(state) {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "green";
    state.snake.forEach(part => {
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

    if (state.gameOver) {
        alert("Game Over! Score: " + state.score);
    }
}

document.addEventListener("keydown", (e) => {
    let direction;

    if (e.key === "ArrowUp") direction = "UP";
    if (e.key === "ArrowDown") direction = "DOWN";
    if (e.key === "ArrowLeft") direction = "LEFT";
    if (e.key === "ArrowRight") direction = "RIGHT";

    if (direction) {
        fetch(BASE_URL + "/direction?dir=" + direction, {
            method: "POST"
        });
    }
});