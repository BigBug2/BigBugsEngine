package engine.objects.renderable.packages;

import engine.controllers.client.MouseInput.MouseEvent;
import engine.managers.client.InputManager;

public class ClientModule extends GameModule {
    private MouseEvent[] mouseEvents = new MouseEvent[0];

    public ClientModule(String name) {
        super(name);
    }

    @Override
    public void update() {
        super.update();
        mouseEvents = InputManager.getMouseLocations(camera);
    }

    @Override
    public void process() {
        processAll();
    }

    public MouseEvent[] getMouseEvents() {
        return mouseEvents;
    }
}
