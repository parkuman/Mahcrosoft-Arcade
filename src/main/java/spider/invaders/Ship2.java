
package spider.invaders;

import spider.invaders.controller.KeyboardController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Spartan Tech
 */
public class Ship2 extends ControlledGameObject {

    ImageIcon ship2 = new ImageIcon(getClass().getResource("/spiderInvadersResources/images/IronSpider.png"));    
    ImageIcon bonusEnemy = new ImageIcon(getClass().getResource("/spiderInvadersResources/images/bonusEnemySkin.gif"));
    ImageIcon lifeCounterShip = new ImageIcon(getClass().getResource("/spiderInvadersResources/images/shipSkinSmall.gif"));

    // Constructor for all ship objects
    public Ship2(int xPosition, int yPosition, Color color, KeyboardController control) {
        super(xPosition, yPosition, color, control);
    }

    // Draw bonus enemy ship
    public void bonusDraw(Graphics g) {

        bonusEnemy.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    // Draw ships for life counter
    public void lifeDraw(Graphics g) {

        lifeCounterShip.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    // Draw player controlled ship
    @Override
    public void draw(Graphics g) {
        ship2.paintIcon(null, g, this.getXPosition(), this.getYPosition());

    }

    // Gets the hit box for all ship objects
    @Override
    public Rectangle getBounds() {
        Rectangle shipHitbox = new Rectangle(this.getXPosition(), this.getYPosition(), 50, 50);
        return shipHitbox;
    }

    // Used to move all ship objects
    @Override
    public void move() {
        // Left arrow key press
        if (control.getKeyStatus(65)) {
            xPos -= 10;
        }
        // Right arrow key press
        if (control.getKeyStatus(68)) {
            xPos += 10;
        }
        
        // Move from edge to edge without stopping
        if (xPos > 800) {
            xPos = -50;
        }
        if (xPos < -50) {
            xPos = 800;
        }
    }
}

