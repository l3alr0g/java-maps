package ui.gfx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    private Vertex[] vertices;
    private int[] indices;
    private Material material;
    /**Vertex array object */
    private int vao; 
    /**Position buffer object */
    private int pbo;
    /**Indices buffer object */
    private int ibo;
    /**Color buffer object */
    private int cbo;
    /**Texture buffer object */
    private int tbo;

    public Mesh(Vertex[] vertices, int[] indices, Material material) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
    }

    // getters and setters

    public int getVAO() {return vao;}
    public int getPBO() {return pbo;}
    public int getIBO() {return ibo;}
    public int getCBO() {return cbo;}
    public int getTBO() {return tbo;}
    public Material getMaterial() {return material;}
    public Vertex[] getVertices() {return vertices;}
    public int[] getIndices() {return indices;}


    // other methods
    /**
     * Generate the mesh from its vertices.
     */
    public void build() {
        material.create();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // position
        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3]; // write to array first, then convert to opengl format
        for (int i = 0; i < vertices.length; i++) {
            positionData[3 * i] = vertices[i].getPos().getX();
            positionData[3 * i + 1] = vertices[i].getPos().getY();
            positionData[3 * i + 2] = vertices[i].getPos().getZ();
        }
        positionBuffer.put(positionData).flip(); // write to FloatBuffer object

        pbo = storeData(positionBuffer, 0, 3); // store data for the shaders to use

        // color
        FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] colorData = new float[vertices.length * 3]; // write to array first, then convert to opengl format
        for (int i = 0; i < vertices.length; i++) {
            colorData[3 * i] = vertices[i].getColor().getX();
            colorData[3 * i + 1] = vertices[i].getColor().getY();
            colorData[3 * i + 2] = vertices[i].getColor().getZ();
        }
        colorBuffer.put(colorData).flip(); // write to FloatBuffer object

        cbo = storeData(colorBuffer, 1, 3); // store data for the shaders to use

        // texture coord
        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
        float[] textureData = new float[vertices.length * 2]; // write to array first, then convert to opengl format
        for (int i = 0; i < vertices.length; i++) {
            textureData[2 * i] = vertices[i].getTexCoord().getX();
            textureData[2 * i + 1] = vertices[i].getTexCoord().getY();
        }
        textureBuffer.put(textureData).flip(); // write to FloatBuffer object

        cbo = storeData(textureBuffer, 2, 2); // store data for the shaders to use



        // indices
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo); // bind buffer (cf https://youtu.be/1F9shq6KubY)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW); // edit buffer data
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); // unbind before next usage
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID); // bind to buffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // store
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0); // cf shader code
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind

        return bufferID;
    }

    public void destroy() {
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(cbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);
        GL30.glDeleteVertexArrays(vao);
        material.destroy();
    }
}