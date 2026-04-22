import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class MenuRenderer {
    private Main main;
    private Rectangle playButton;
    private Rectangle helpButton;
    private Rectangle quitButton;
    private Rectangle singlePlayerButton;
    private Rectangle twoPlayerButton;
    private boolean showHelpScreen = false;
    private boolean showModeSelection = false;

    public MenuRenderer(Main main) {
        this.main = main;
        initButtons();
    }

    private void initButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (Main.GRID_SIZE * Main.CELL_SIZE) / 2;
        int startY = Main.GRID_SIZE * Main.CELL_SIZE / 2 - 60;

        playButton = new Rectangle(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight);
        helpButton = new Rectangle(centerX - buttonWidth / 2, startY + 70, buttonWidth, buttonHeight);
        quitButton = new Rectangle(centerX - buttonWidth / 2, startY + 140, buttonWidth, buttonHeight);

        int modeButtonWidth = 250;
        int modeButtonHeight = 60;
        int modeStartY = Main.GRID_SIZE * Main.CELL_SIZE / 2 - 80;
        singlePlayerButton = new Rectangle(centerX - modeButtonWidth / 2, modeStartY, modeButtonWidth, modeButtonHeight);
        twoPlayerButton = new Rectangle(centerX - modeButtonWidth / 2, modeStartY + 80, modeButtonWidth, modeButtonHeight);
    }

    public void render() {
        if (showHelpScreen) {
            drawHelpScreen();
        } else if (showModeSelection) {
            drawModeSelection();
        } else {
            drawMainMenu();
        }
    }

    private void drawMainMenu() {
        main.changeColor(main.green);
        main.drawBoldText(main.width() / 2 - 100, main.height() / 2 - 100, "SNAKE GAME", "Arial", 50);

        // Play button
        main.changeColor(new Color(0, 200, 0));
        main.drawSolidRectangle(playButton.x, playButton.y, playButton.width, playButton.height);
        main.changeColor(main.black);
        main.drawRectangle(playButton.x, playButton.y, playButton.width, playButton.height, 3);
        main.changeColor(main.white);
        main.drawBoldText(playButton.x + 65, playButton.y + 35, "PLAY", "Arial", 28);

        // Help button
        main.changeColor(new Color(0, 100, 200));
        main.drawSolidRectangle(helpButton.x, helpButton.y, helpButton.width, helpButton.height);
        main.changeColor(main.black);
        main.drawRectangle(helpButton.x, helpButton.y, helpButton.width, helpButton.height, 3);
        main.changeColor(main.white);
        main.drawBoldText(helpButton.x + 60, helpButton.y + 35, "HELP", "Arial", 28);

        // Quit button
        main.changeColor(new Color(200, 0, 0));
        main.drawSolidRectangle(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        main.changeColor(main.black);
        main.drawRectangle(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 3);
        main.changeColor(main.white);
        main.drawBoldText(quitButton.x + 60, quitButton.y + 35, "QUIT", "Arial", 28);
    }

    private void drawModeSelection() {
        main.changeColor(main.green);
        main.drawBoldText(main.width() / 2 - 150, main.height() / 2 - 150, "SELECT GAME MODE", "Arial", 40);

        // Single Player button
        main.changeColor(new Color(0, 150, 255));
        main.drawSolidRectangle(singlePlayerButton.x, singlePlayerButton.y, singlePlayerButton.width, singlePlayerButton.height);
        main.changeColor(main.black);
        main.drawRectangle(singlePlayerButton.x, singlePlayerButton.y, singlePlayerButton.width, singlePlayerButton.height, 3);
        main.changeColor(main.white);
        main.drawBoldText(singlePlayerButton.x + 35, singlePlayerButton.y + 40, "SINGLE PLAYER", "Arial", 24);

        // Two Player button
        main.changeColor(new Color(255, 150, 0));
        main.drawSolidRectangle(twoPlayerButton.x, twoPlayerButton.y, twoPlayerButton.width, twoPlayerButton.height);
        main.changeColor(main.black);
        main.drawRectangle(twoPlayerButton.x, twoPlayerButton.y, twoPlayerButton.width, twoPlayerButton.height, 3);
        main.changeColor(main.white);
        main.drawBoldText(twoPlayerButton.x + 35, twoPlayerButton.y + 40, "TWO PLAYERS", "Arial", 24);

        main.changeColor(new Color(150, 150, 150));
        main.drawText(main.width() / 2 - 100, main.height() - 50, "Click outside to go back", "Arial", 16);
    }

    private void drawHelpScreen() {
        main.changeColor(new Color(20, 20, 20));
        main.drawSolidRectangle(0, 0, main.width(), main.height());

        main.changeColor(main.green);
        main.drawBoldText(main.width() / 2 - 80, 50, "HELP", "Arial", 40);

        main.changeColor(main.yellow);
        main.drawBoldText(main.width() / 2 - 120, 90, "Game Objects:", "Arial", 20);
        main.changeColor(main.white);
        main.drawText(main.width() / 2 - 220, 115, "Eat Apples to grow and earn points. Avoid poison apples!", "Arial", 18);
        main.drawText(main.width() / 2 - 200, 140, "Don't crash to the walls or yourself. You have 5 lives", "Arial", 18);

        main.changeColor(main.yellow);
        main.drawBoldText(main.width() / 2 - 120, 180, "Single Player Controls:", "Arial", 24);
        main.changeColor(main.white);
        main.drawBoldText(main.width() / 2 - 180, 205, "Arrow Keys: Move the snake", "Arial", 18);

        main.changeColor(main.yellow);
        main.drawBoldText(main.width() / 2 - 120, 230, "Two Player Controls:", "Arial", 24);
        main.changeColor(main.blue);
        main.drawText(main.width() / 2 - 220, 250, "Player 1 (blue): Arrow keys to move", "Arial", 18);
        main.changeColor(main.green);
        main.drawText(main.width() / 2 - 220, 280, "Player 2 (green): WASD keys to move", "Arial", 18);

        main.changeColor(main.yellow);
        main.drawBoldText(main.width() / 2 - 120, 320, "General Controls:", "Arial", 24);
        main.changeColor(main.white);
        main.drawText(main.width() / 2 - 180, 350, "P - Pause/Resume game", "Arial", 18);
        main.drawText(main.width() / 2 - 180, 385, "R - Restart game (when game over)", "Arial", 18);
        main.drawText(main.width() / 2 - 180, 420, "M - Return to menu (when game is over)", "Arial", 18);

        // Back button
        int backBtnWidth = 150;
        int backBtnHeight = 45;
        int backBtnX = main.width() / 2 - backBtnWidth / 2;
        int backBtnY = main.height() - 100;

        main.changeColor(new Color(100, 100, 100));
        main.drawSolidRectangle(backBtnX, backBtnY, backBtnWidth, backBtnHeight);
        main.changeColor(main.black);
        main.drawRectangle(backBtnX, backBtnY, backBtnWidth, backBtnHeight, 2);
        main.changeColor(main.white);
        main.drawBoldText(backBtnX + 35, backBtnY + 32, "BACK", "Arial", 24);
    }

    public void handleMouseClick(MouseEvent event) {
        if (showHelpScreen) {
            int backBtnWidth = 150;
            int backBtnHeight = 45;
            int backBtnX = main.width() / 2 - backBtnWidth / 2;
            int backBtnY = main.height() - 100;
            Rectangle backBtn = new Rectangle(backBtnX, backBtnY, backBtnWidth, backBtnHeight);
            if (backBtn.contains(event.getPoint())) {
                showHelpScreen = false;
            }
        } else if (showModeSelection) {
            if (singlePlayerButton.contains(event.getX(), event.getY())) {
                main.startSinglePlayer();
                showModeSelection = false;
            } else if (twoPlayerButton.contains(event.getX(), event.getY())) {
                main.startTwoPlayer();
                showModeSelection = false;
            } else {
                showModeSelection = false;
            }
        } else {
            if (playButton.contains(event.getX(), event.getY())) {
                showModeSelection = true;
            } else if (helpButton.contains(event.getX(), event.getY())) {
                showHelpScreen = true;
            } else if (quitButton.contains(event.getX(), event.getY())) {
                System.exit(0);
            }
        }
    }
}