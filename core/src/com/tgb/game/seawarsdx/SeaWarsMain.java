package com.tgb.game.seawarsdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SeaWarsMain extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture backgroundBg;
    private TextureRegion background;
    private TextureRegion[] sprites;
    private float[] spriteX;
    private float[] spriteY;
    private float[] spriteSpeedX;
    private float[] spriteSpeedY;
    private Music music;
    private Sound sound1;
    private Sound sound2;

    // Slider variables
    private Stage stage;
    private Slider slider;
    private BitmapFont font;

    // Toggle button variables
    private CheckBox textToggleButton;
    private CheckBox spriteToggleButton;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load background
        Texture bgTexture = new Texture("images/Water3.png");
        backgroundBg = bgTexture;
        background = new TextureRegion(bgTexture);

        // Load sprites
        Texture spriteTexture = new Texture("images/Aerogli.png");
        int numSprites = 3;
        sprites = new TextureRegion[numSprites];
        spriteX = new float[numSprites];
        spriteY = new float[numSprites];
        spriteSpeedX = new float[numSprites];
        spriteSpeedY = new float[numSprites];

        for (int i = 0; i < numSprites; i++) {
            sprites[i] = new TextureRegion(spriteTexture);
            spriteX[i] = MathUtils.random(0, Gdx.graphics.getWidth() - sprites[i].getRegionWidth());
            spriteY[i] = MathUtils.random(0, Gdx.graphics.getHeight() - sprites[i].getRegionHeight());
            spriteSpeedX[i] = MathUtils.random(50, 150);
            spriteSpeedY[i] = MathUtils.random(50, 150);
        }

        // Load sounds
        music = Gdx.audio.newMusic(Gdx.files.internal("music/A_Mission.mp3"));
        sound1 = Gdx.audio.newSound(Gdx.files.internal("sound/SubLaunch3p.wav"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("sound/SubLaunchP.wav"));

        // Play background music
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        // Create the stage and slider
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        //NinePatchDrawable drawable = new NinePatchDrawable(new NinePatch(new Texture("images/SpeedBarre.png"), 6, 6, 6, 6));
        skin.add("background", new Texture("images/SpeedBarre.png"));

        //Drawable knob = new NinePatchDrawable(new NinePatch(new Texture("images/SpeedCursor.png"), 6, 6, 6, 6));
        skin.add("knob", new TextureRegion(new Texture("images/SpeedCursor.png"), 0, 0, 10, 10));

        SliderStyle style = new SliderStyle(skin.getDrawable("background"), skin.getDrawable("knob"));
        slider = new Slider(0, 100, 1, false, style);
        slider.setWidth(Gdx.graphics.getWidth() * 0.75f);
        slider.setPosition(Gdx.graphics.getWidth() * 0.125f, Gdx.graphics.getHeight() * 0.9f);

        // Create a BitmapFont to display the slider value
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        // Create the toggle buttons
        Drawable checkboxBackground = new TextureRegionDrawable(new TextureRegion(new Texture("Speaker.png")));
        Drawable checkboxKnob = new TextureRegionDrawable(new TextureRegion(new Texture("SpeakerOff.png")));
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(checkboxBackground, checkboxKnob, font, Color.WHITE);
        textToggleButton = new CheckBox("Toggle Text", checkBoxStyle);
        textToggleButton.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.7f);
        textToggleButton.getLabel().setFontScale(0.8f); // Adjust the font scale if needed

        // For the sprite toggle button, we'll use one of the loaded sprites as the drawable
        Drawable spriteDrawable = new TextureRegionDrawable(sprites[0]); // Use the first sprite as an example
        CheckBox.CheckBoxStyle spriteToggleButtonStyle = new CheckBox.CheckBoxStyle(spriteDrawable, checkboxKnob, font, Color.WHITE);
        spriteToggleButton = new CheckBox("", spriteToggleButtonStyle);
        spriteToggleButton.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.5f);
        spriteToggleButton.setSize(100, 100); // Adjust the size of the toggle button if needed

        // Add the actors to the stage
        stage.addActor(slider);
        stage.addActor(textToggleButton);
        stage.addActor(spriteToggleButton);
    }

    @Override
    public void render() {
        // Update the stage to handle input events
        stage.act(Gdx.graphics.getDeltaTime());

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update sprite positions
        for (int i = 0; i < sprites.length; i++) {
            spriteX[i] += spriteSpeedX[i] * Gdx.graphics.getDeltaTime();
            spriteY[i] += spriteSpeedY[i] * Gdx.graphics.getDeltaTime();

            // Wrap the sprites around the screen
            if (spriteX[i] > Gdx.graphics.getWidth())
                spriteX[i] = -sprites[i].getRegionWidth();
            if (spriteX[i] < -sprites[i].getRegionWidth())
                spriteX[i] = Gdx.graphics.getWidth();
            if (spriteY[i] > Gdx.graphics.getHeight())
                spriteY[i] = -sprites[i].getRegionHeight();
            if (spriteY[i] < -sprites[i].getRegionHeight())
                spriteY[i] = Gdx.graphics.getHeight();
        }

        // Play sounds randomly
        if (MathUtils.randomBoolean(0.01f))
            sound1.play(0.5f);
        if (MathUtils.randomBoolean(0.01f))
            sound2.play(0.5f);

        // Draw everything
        batch.begin();
        batch.draw(backgroundBg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < sprites.length; i++) {
            // Set sprite transparency using its alpha channel
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(sprites[i], spriteX[i], spriteY[i]);
            batch.setColor(1, 1, 1, 1); // Reset the batch color
        }

        // Draw the slider value in the top left corner
        font.draw(batch, "Slider Value: " + (int) slider.getValue(), 20, Gdx.graphics.getHeight() - 20);

        batch.end();

        // Draw the stage
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.getTexture().dispose();
        for (TextureRegion sprite : sprites) {
            sprite.getTexture().dispose();
        }
        music.dispose();
        sound1.dispose();
        sound2.dispose();
        stage.dispose();
        font.dispose();
    }
}