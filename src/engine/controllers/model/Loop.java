package engine.controllers.model;

import engine.ESource;
import engine.controllers.view.View;
import engine.managers.client.InputManager;
import engine.objects.GameObject;
import engine.objects.renderable.packages.GameModule;

public class Loop {
        private static final int MAX_UPDATES = 3;
        private static int targetFPS = 60;
        private static int targetTime = 1000000000 / targetFPS;

        private static long lastUpdateTime;

        private static boolean running;

        public static void init() {}

        public static void startGameLoop() {
            running = true;

            lastUpdateTime = System.nanoTime();

            while (running) {
                InputManager.update();

                long currentTime = System.nanoTime();

                int updates = 0;

                while (currentTime - lastUpdateTime >= targetTime) {
                    for (GameObject obj: ESource.MODULES.getAll()) {
                        GameModule module = (GameModule) obj;
                        if (module != null) {
                            module.update();
                            CollisionsController.process();
                            if (module.isProcessable()) {
                                module.process();
                            }
                        }
                    }

                    lastUpdateTime += targetTime;
                    updates++;

                    if (updates > MAX_UPDATES) {
                        break;
                    }
                }

                View.render();

                long timeTaken = System.nanoTime() - currentTime;
                if (targetTime > timeTaken) {
                    try {
                        Thread.sleep((targetTime - timeTaken) / 1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
}
