package io.github.kuggek.editor.misc;

import io.github.kuggek.engine.ecs.GameObject;
import io.github.kuggek.engine.ecs.components.rendering.CameraComponent;
import io.github.kuggek.engine.scripting.KeyInput;

import static java.awt.event.KeyEvent.*;

public class EditorCamera extends CameraComponent {
    private static final GameObject empty = new GameObject(-1);
    float movementSpeed;
    float rotationSpeed = 2.5f;

    float forwardVel;
    float rightVel;
    float rotY;
    float rotX;
    float horizontalVel;

    boolean disableMovement = false;

    public EditorCamera() {
        super(empty, 50f, 0.1f, 2500f, false);
    }

    public void update(float deltaTime, KeyInput keyInput) {
        if (disableMovement) {
            return;
        }

        movementSpeed = 10;

        if (keyInput.isKeyHeld(VK_SHIFT)) {
            movementSpeed = 2.5f;
        } else if (keyInput.isKeyHeld(VK_CONTROL)) {
            movementSpeed = 35;
        }

        

        forwardVel = 0;

        if (keyInput.isKeyHeld(VK_W)) {
            forwardVel += movementSpeed;
        }
        if (keyInput.isKeyHeld(VK_S)) {
            forwardVel -= movementSpeed;
        }

        rightVel = 0;

        if (keyInput.isKeyHeld(VK_D)) {
            rightVel += movementSpeed;
        }
        if (keyInput.isKeyHeld(VK_A)) {
            rightVel -= movementSpeed;
        }

        if (forwardVel != 0) {
            transform.moveTowards(transform.getForward(), forwardVel * deltaTime);
        }

        if (rightVel != 0) {
            transform.moveTowards(transform.getRight(), rightVel * deltaTime);
        }

        rotY = 0;

        if (keyInput.isKeyHeld(VK_RIGHT)) {
            rotY -= rotationSpeed;
        }
        if (keyInput.isKeyHeld(VK_LEFT)) {
            rotY += rotationSpeed;
        }

        if (rotY != 0) {
            transform.setRotation(transform.getRotation().rotateLocalY(rotY * deltaTime));
        }

        rotX = 0;

        if (keyInput.isKeyHeld(VK_UP)) {
            rotX += rotationSpeed;
        }
        if (keyInput.isKeyHeld(VK_DOWN)) {
            rotX -= rotationSpeed;
        }

        if (rotX != 0) {
            transform.setRotation(transform.getRotation().rotateX(rotX * deltaTime));
        }

        horizontalVel = 0;

        if (keyInput.isKeyHeld(VK_SPACE)) {
            horizontalVel += movementSpeed;
        }

        if (keyInput.isKeyHeld(VK_C)) {
            horizontalVel -= movementSpeed;
        }

        if (horizontalVel != 0) {
            transform.moveTowards(transform.getUp(), horizontalVel * deltaTime);
        }
    }

    public void setDisableMovement(boolean disableMovement) {
        this.disableMovement = disableMovement;
    }

}
