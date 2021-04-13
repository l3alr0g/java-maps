import org.lwjgl.glfw.GLFW;

import exceptions.ExitOverrideException;
import ui.Task;
import ui.Window;
import ui.gfx.Mesh;
import ui.gfx.Vertex;
import ui.gfx.Renderer;
import ui.gfx.Shader;
import ui.math.Vector3f;

/**
 * Main app class
 */
public class App implements Runnable {

    private Thread main; // main thread
    private Window window;
    private Renderer renderer;
    private final int[] windowSize = {1600, 900};

    // testing code ##########################
    private Mesh mesh = new Mesh(new Vertex[] {
        new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f)),
        new Vertex(new Vector3f(0.5f, 0.5f, 0.0f)),
        new Vertex(new Vector3f(0.5f, -0.5f, 0.0f)),
        new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f))
    }, 
    
    new int[] {
        0, 1, 2,
        0, 3, 2
    });

    private Shader testShader = new Shader("./src/main/java/resources/shaders/Vertex.glsl", "./src/main/java/resources/shaders/Fragment.glsl");

    // ###############################

    public void start() {
        main = new Thread(this, "main");
        main.start();
    }

    /**
     * App boot up sequence.
     */
    public void init() {
        System.out.println("Booting up application...");

        renderer = new Renderer(testShader);
        window = new Window(windowSize[0], windowSize[1], "7map test application");
        window.setBackgroundColor(0.1f, 0.1f, 0.1f);
        window.create();

        // testing code goes here #################
        mesh.build();

        testShader.create();
        // ############################


        // schedule window closing when escape is pressed
        Task closeOnEsc = window.onKeyDown(GLFW.GLFW_KEY_ESCAPE, () -> {
            throw new ExitOverrideException(0);
        });

        Task fullscreen = window.onKeyDown(GLFW.GLFW_KEY_F11, () -> {
            window.setFullscreen(!window.isFullscreen());
        });

        // Task dummy = window.scheduleTask(() -> {
        //     System.out.println("Heyo");
        // });
    }

    /**
     * Implementation of the Interface-specific method Runnable.run
     */
    public void run() {
        init();
        try {
            while(!window.shouldClose()) {
                update();
                render();
            }
        } catch (ExitOverrideException e) {
            System.out.println(e.getMessage());
        }
        window.destroy(); // absolutely necessary
        main.interrupt();
    }

    private void update() {
        window.update();
    }

    private void render() {
        renderer.renderMesh(mesh);
        window.swap();
    }

    public static void main(String[] args) {
        new App().start();
    }

}
