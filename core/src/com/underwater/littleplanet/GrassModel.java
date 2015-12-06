package com.underwater.littleplanet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by azakhary on 9/17/2015.
 */
public class GrassModel {

    public Mesh mesh;

    private Array<Quad> quads = new Array<Quad>();


    public GrassModel() {
        generateMesh();
    }

    public class Vertex {
        public Vector3 coord;
        public Vector2 texCoord;
        public Vector3 normal;

        public Vertex(Vector3 coord, Vector2 texCoord, Vector3 normal) {
            this.coord = coord;
            this.texCoord = texCoord;
            this.normal = normal;
        }
    }


    public class Quad {
        public Vertex v1;
        public Vertex v2;
        public Vertex v3;
        public Vertex v4;

        public Vector3 normal;

        public Quad(Vector3 pos, float angle) {
            float size = MathUtils.random(0.5f, 1.7f);
            Vector3 pos1 = new Vector3(pos.x-MathUtils.cosDeg(angle) * size, pos.y-size, pos.z+MathUtils.sinDeg(angle) * size);
            Vector3 pos2 = new Vector3(pos.x-MathUtils.cosDeg(angle) * size, pos.y+size, pos.z+MathUtils.sinDeg(angle) * size);
            Vector3 pos3 = new Vector3(pos.x+MathUtils.cosDeg(angle) * size, pos.y+size, pos.z-MathUtils.sinDeg(angle) * size);
            Vector3 pos4 = new Vector3(pos.x+MathUtils.cosDeg(angle) * size, pos.y-size, pos.z-MathUtils.sinDeg(angle) * size);

            Vector3 pos2c = new Vector3(pos2);
            Vector3 pos3c = new Vector3(pos3);
            normal = pos2c.sub(pos1).crs(pos3c.sub(pos1));

            this.v1 = new Vertex(pos1, new Vector2(0, 0), normal);
            this.v2 = new Vertex(pos2, new Vector2(0, 1), normal);
            this.v3 = new Vertex(pos3, new Vector2(1, 1), normal);
            this.v4 = new Vertex(pos4, new Vector2(1, 0), normal);
        }
    }


    private void generateMesh() {


        for(float i = 0; i < 20; i+=0.5f) {
            for(float j = 0; j < 20; j+=0.5f) {
                float offX = MathUtils.random(-0.3f, 0.3f);
                float offZ = MathUtils.random(-0.3f, 0.3f);
                quads.add(new Quad(new Vector3(i+offX, 0, j+offZ), 0));
                quads.add(new Quad(new Vector3(i+offX, 0, j+offZ), 45));
                quads.add(new Quad(new Vector3(i+offX, 0, j+offZ), -45));
            }
        }

        System.out.println("Triangles: " + quads.size*2);

/**
        quads.add(new Quad(new Vector3(0, 0, 0), 0));
        quads.add(new Quad(new Vector3(0, 0, 0), 45));
        quads.add(new Quad(new Vector3(0, 0, 0), -45));
*/

        int attrCount = 8;
        float[] vertices = new float[quads.size*4*attrCount];
        short[] indices = new short[quads.size*6];
        mesh = new Mesh(true, quads.size*4*attrCount, quads.size*6,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));

        int vidx = 0;
        int iidx = 0;
        int vert = -1;
        for(Quad tmpQuad: quads) {
            vert++;
            vertices[vidx++] = tmpQuad.v1.coord.x;
            vertices[vidx++] = tmpQuad.v1.coord.y;
            vertices[vidx++] = tmpQuad.v1.coord.z;
            vertices[vidx++] = tmpQuad.v1.texCoord.x;
            vertices[vidx++] = tmpQuad.v1.texCoord.y;
            vertices[vidx++] = tmpQuad.normal.x;
            vertices[vidx++] = tmpQuad.normal.y;
            vertices[vidx++] = tmpQuad.normal.z;

            vert++;
            vertices[vidx++] = tmpQuad.v2.coord.x;
            vertices[vidx++] = tmpQuad.v2.coord.y;
            vertices[vidx++] = tmpQuad.v2.coord.z;
            vertices[vidx++] = tmpQuad.v2.texCoord.x;
            vertices[vidx++] = tmpQuad.v2.texCoord.y;
            vertices[vidx++] = tmpQuad.normal.x;
            vertices[vidx++] = tmpQuad.normal.y;
            vertices[vidx++] = tmpQuad.normal.z;

            vert++;
            vertices[vidx++] = tmpQuad.v3.coord.x;
            vertices[vidx++] = tmpQuad.v3.coord.y;
            vertices[vidx++] = tmpQuad.v3.coord.z;
            vertices[vidx++] = tmpQuad.v3.texCoord.x;
            vertices[vidx++] = tmpQuad.v3.texCoord.y;
            vertices[vidx++] = tmpQuad.normal.x;
            vertices[vidx++] = tmpQuad.normal.y;
            vertices[vidx++] = tmpQuad.normal.z;

            vert++;
            vertices[vidx++] = tmpQuad.v4.coord.x;
            vertices[vidx++] = tmpQuad.v4.coord.y;
            vertices[vidx++] = tmpQuad.v4.coord.z;
            vertices[vidx++] = tmpQuad.v4.texCoord.x;
            vertices[vidx++] = tmpQuad.v4.texCoord.y;
            vertices[vidx++] = tmpQuad.normal.x;
            vertices[vidx++] = tmpQuad.normal.y;
            vertices[vidx++] = tmpQuad.normal.z;

            indices[iidx++] = (short) (vert-3);
            indices[iidx++] = (short) (vert-2);
            indices[iidx++] = (short) vert;
            indices[iidx++] = (short) vert;
            indices[iidx++] = (short) (vert-2);
            indices[iidx++] = (short) (vert-1);
        }

        mesh.setVertices(vertices);
        mesh.setIndices(indices);
    }


}
