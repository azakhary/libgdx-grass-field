package com.underwater.littleplanet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class MainGame extends ApplicationAdapter {

	private Camera camera;
	private RenderContext renderContext;
	private Renderable renderable;

	private ModelInstance atmoInstance;
	private ShaderProgram atmoShader;
	private Renderable atmoR;

	private Model atmosphere;
	private ModelInstance planetInstance;
	private Environment environment;

	private CameraController camController;
	private DefaultShader shader;
	private DefaultShader atmosphereShader;

	private Mesh mesh;

	private float rotation = 0;

	private ModelBatch modelBatch;

	Texture texture;

	private float temperature = 7f;
	private float temperatureSpeed = -0.7f;

	private Texture atmMap;

	private float time = 0;

	private Texture grassTexture;
	private Texture noiseTexture;

	private float verticalSpeed = 0;

	@Override
	public void create () {
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, -1f, 10f);
		camera.rotate(camera.position, 180);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		camera.update();

		//((OrthographicCamera)camera).zoom = 1f/30f;

		camController = new CameraController(camera);
		camController.target.set(camera.position);
		Gdx.input.setInputProcessor(camController);

		grassTexture = new Texture(Gdx.files.internal("grass.png"));
		noiseTexture = new Texture(Gdx.files.internal("noise.png"));


		Material material = new Material(IntAttribute.createCullFace(GL20.GL_NONE));
		BlendingAttribute blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//material.set(blendingAttribute);
		//material.set(TextureAttribute.createDiffuse(noiseTexture));


		modelBatch = new ModelBatch();

		GrassModel grass = new GrassModel();
		//Mesh atmosphereMesh = atmosphere.meshParts.get(0).mesh;


		mesh = grass.mesh;

		//planetInstance = new ModelInstance(planet, 0f, 0f, 0f);
		//environment = new Environment();
		//environment.set(ColorAttribute.createAmbient(1f, 1f, 1f, 1f));
		//environment.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, -1f, -0.8f, -0.2f));

		//NodePart blockPart = planet.nodes.get(0).parts.get(0);
		renderable = new Renderable();
		renderable.mesh = mesh;
		renderable.primitiveType = GL20.GL_TRIANGLES;
		renderable.material = material;
		renderable.meshPartSize = mesh.getNumVertices()*mesh.getVertexSize();
		//planetInstance.getRenderable(renderable);
		//renderable.environment = environment;
		renderable.worldTransform.idt();


		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		String vert = Gdx.files.internal("grass.vert").readString();
		String frag = Gdx.files.internal("grass.frag").readString();
		String vert2 = Gdx.files.internal("shader.vert").readString();
		String frag2 = Gdx.files.internal("shader.frag").readString();
		shader = new DefaultShader(renderable, new DefaultShader.Config(vert, frag));
		//shader = new DefaultShader(renderable, new DefaultShader.Config(vert2, frag2));
		//shader = new DefaultShader(renderable);
		shader.init();
		//DefaultShader.defaultDepthFunc = 0;

		//Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);

		//texture.bind();

		//Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);

		camera.update();
	}

	@Override
	public void render () {

		time += Gdx.graphics.getDeltaTime();

		camController.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));


		//Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
		//noiseTexture.bind();

		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		grassTexture.bind();

		renderContext.begin();
		shader.begin(camera, renderContext);

		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		//Gdx.graphics.getGL20().glCullFace(GL20.GL_NONE );
		//Gdx.graphics.getGL20().glDisable(GL20.GL_CULL_FACE );
		Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

//		((DefaultShader)modelBatch.getShaderProvider().getShader(atmoR)).program.setUniformi("u_texture2", 0);

		//shader.program.setUniformi("u_noise", 1);
		shader.program.setUniformi("u_texture", 0);

		shader.program.setUniformf("u_ambientColor", new Color(0.9f, 0.9f, 0.9f, 0.3f));

		shader.program.setUniformf("u_camera", camera.position);

		shader.program.setUniformf("u_time", time);
				shader.render(renderable);
		//mesh.render(shader., GL20.GL_TRIANGLES, 0, 3);
		shader.end();
		renderContext.end();

		modelBatch.begin(camera);

		//modelBatch.render(atmoInstance);
		modelBatch.end();

		//renderable.worldTransform.setToRotation(Vector3.Y, rotation);

		/*
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			verticalSpeed = 12;
		}

		verticalSpeed -= 25 * Gdx.graphics.getDeltaTime();

		camera.position.y-=verticalSpeed*Gdx.graphics.getDeltaTime();

		if(camera.position.y > -1f) {
			camera.position.y = -1f;
			verticalSpeed = 0;
		}

		camController.target.set(camera.position);

		camera.update();
		*/

	}

	@Override
	public void dispose () {
		shader.dispose();
	}
}
