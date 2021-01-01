package com.richikin.jetman.developer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.ui.DefaultPanel;
import com.richikin.jetman.ui.Scene2DUtils;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class DeveloperPanel extends DefaultPanel
{
    private static final DeveloperPanel _INSTANCE;

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new DeveloperPanel();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static DeveloperPanel inst()
    {
        return _INSTANCE;
    }

    //
    // The elements below are all initialised in quickSetup()
    private int glProfilerRow;
    private int glProfilerColumn;
    private int devModeRow;
    private int devModeColumn;
    private int godModeRow;
    private int godModeColumn;

    static class DMEntry
    {
        final String  string;
        final String  prefName;

        DMEntry(String _string, String _pref)
        {
            this.string   = _string;
            this.prefName = _pref;
        }
    }

    private static final int _TABLE_COLUMNS = 3;

    private final String _DEVMODE = "devmode";
    private final String _GODMODE = "godmode";

    private DMEntry[][] devMenu;

    private Texture      foreground;
    private CheckBox[][] buttons;
    private TextField    heading;
    private Table        table;
    private GLProfiler   glProfiler;
    private TextButton   exitButton;
    private TextButton   buttonResetPrefs;
    private TextButton   buttonResetHiScores;
    private TextButton   buttonResetStats;
    private TextButton   buttonGLProfiler;
    private TextButton   buttonCollisionDump;

    private boolean okToResetPrefs;

    public DeveloperPanel()
    {
        super();

        // OpenGL Profiler
        if (Developer.isDevMode())
        {
            glProfiler = new GLProfiler(Gdx.graphics);
        }
    }

    public void setup()
    {
        Trace.__FILE_FUNC();

        AppConfig.gamePaused = true;

        loadDevMenu();

        quickSetup();

        foreground = App.assets.loadSingleAsset("data/empty_screen.png", Texture.class);

        okToResetPrefs = false;

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        table = createTable();
        createHeading(skin);
        createButtons(skin);

        populateTable(table, skin);

        // Wrap the table in a scrollpane.
        scrollPane = new ScrollPane(table, skin);

        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 400);
        scrollPane.setHeight((float) Gfx._HUD_HEIGHT - 200);
        scrollPane.setPosition(AppConfig.hudOriginX + 200, AppConfig.hudOriginY + 100);
        scrollPane.setScrollbarsOnTop(true);

        App.stage.addActor(scrollPane);
        App.stage.addActor(heading);
        App.stage.addActor(exitButton);
        App.stage.addActor(buttonResetPrefs);
        App.stage.addActor(buttonResetHiScores);
        App.stage.addActor(buttonResetStats);
        App.stage.addActor(buttonGLProfiler);
        App.stage.addActor(buttonCollisionDump);

        updatePreferencesOnEntry();

        if (Developer.isDevMode())
        {
            glProfilerUpdate();
        }
    }

    @Override
    public void close()
    {
        updatePreferencesOnExit();

        Developer.developerPanelActive = false;

        clearActors();
    }

    private Table createTable()
    {
        Table table = new Table();
        table.top().left();
        table.pad(20, 10, 10, 10);

        Texture texture    = App.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image   background = new Image(new TextureRegion(texture));
        table.setBackground(background.getDrawable());

        return table;
    }

    private void createHeading(Skin skin)
    {
        heading = new TextField("DEVELOPER OPTIONS", skin);
        heading.setWidth(180);
        heading.setPosition(AppConfig.hudOriginX + 610, AppConfig.hudOriginY + 680, Align.center);
        heading.setDisabled(true);
    }

    private void createButtons(Skin skin)
    {
        exitButton          = new TextButton("Back", skin);
        buttonResetPrefs    = new TextButton("Reset Preferences To Default", skin);
        buttonResetHiScores = new TextButton("Reset HiScore Table", skin);
        buttonResetStats    = new TextButton("Reset Stats Meters", skin);
        buttonGLProfiler    = new TextButton("GLProfiler Dump", skin);
        buttonCollisionDump = new TextButton("CollisionObject Breakdown", skin);

        int x = 20;

        buttonResetPrefs.setPosition(AppConfig.hudOriginX + x, AppConfig.hudOriginY + 15);

        x += buttonResetPrefs.getWidth() + 20;

        buttonResetHiScores.setPosition(AppConfig.hudOriginX + x, AppConfig.hudOriginY + 15);

        x += buttonResetHiScores.getWidth() + 20;

        buttonResetStats.setPosition(AppConfig.hudOriginX + x, AppConfig.hudOriginY + 15);

        x += buttonResetStats.getWidth() + 20;

        buttonGLProfiler.setPosition(AppConfig.hudOriginX + x, AppConfig.hudOriginY + 15);

        x += buttonGLProfiler.getWidth() + 20;

        buttonCollisionDump.setPosition(AppConfig.hudOriginX + x, AppConfig.hudOriginY + 15);

        exitButton.setPosition(AppConfig.hudOriginX + 20, AppConfig.hudOriginY + 620);
        exitButton.setSize(48, 48);

        createButtonListeners();
    }

    private void populateTable(Table table, Skin skin)
    {
        TextField[] label = new TextField[_TABLE_COLUMNS];

        buttons = new CheckBox[devMenu.length][_TABLE_COLUMNS];

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                label[column] = new TextField(devMenu[row][column].string, skin);
                label[column].setAlignment(Align.center);
                label[column].setDisabled(true);

                buttons[row][column] = scene2DUtils.addCheckBox("toggle_on", "toggle_off", 0, 0, Color.WHITE, skin);
                buttons[row][column].setChecked(App.settings.isEnabled(devMenu[row][column].prefName));
            }

            createCheckBoxListener(row);

            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                Label num = new Label("" + row + ": ", skin);
                table.add(num).padLeft(10);
                table.add(label[column]).prefWidth(90);
                table.add(buttons[row][column]).prefWidth(50);

                if ("".equals(label[column].getText()))
                {
                    num.setVisible(false);
                    label[column].setVisible(false);
                    buttons[row][column].setVisible(false);
                }
            }

            table.row();
        }

        table.setVisible(true);
    }

    public void createButtonListeners()
    {
        exitButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                close();
            }
        });

        buttonResetPrefs.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resetPreferencesToDefaults();
            }
        });

        buttonResetStats.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Stats.resetAllMeters();
            }
        });

        buttonResetHiScores.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (Developer.isDevMode())
                {
                    App.highScoreUtils.resetTable();

                    Trace.__FILE_FUNC("HISCORE Table reset to defaults.");
                }
            }
        });

        buttonGLProfiler.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (Developer.isDevMode())
                {
                    Trace.dbg
                        (
                            "  Drawcalls: " + glProfiler.getDrawCalls()
                                + ", Calls: " + glProfiler.getCalls()
                                + ", TextureBindings: " + glProfiler.getTextureBindings()
                                + ", ShaderSwitches:  " + glProfiler.getShaderSwitches()
                                + "vertexCount: " + glProfiler.getVertexCount().value
                        );

                    glProfiler.reset();
                }
            }
        });
    }

    private void createCheckBoxListener(int index)
    {
        for (int column = 0; column < _TABLE_COLUMNS; column++)
        {
            buttons[index][column].addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (!okToResetPrefs)
                    {
                        updatePreferences();
                    }
                }
            });
        }
    }

    public boolean update()
    {
        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                buttons[row][column].setText(buttons[row][column].isChecked() ? " ON" : " OFF");
            }
        }

        return false;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, AppConfig.hudOriginX, AppConfig.hudOriginY);
        }
    }

    private void updatePreferencesOnEntry()
    {
        if (!Developer.isDevMode())
        {
            App.settings.getPrefs().putBoolean(Settings._MENU_HEAPS, false);
            App.settings.getPrefs().flush();
        }

        buttons[devModeRow][devModeColumn].setChecked(Developer.isDevMode());
        buttons[godModeRow][godModeColumn].setChecked(Developer.isGodMode());

        updatePreferences();
    }

    private void updatePreferences()
    {
        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                buttons[row][column].setText(buttons[row][column].isChecked() ? " ON" : " OFF");
                App.settings.getPrefs().putBoolean(devMenu[row][column].prefName, buttons[row][column].isChecked());
            }
        }

        if (Developer.isDevMode())
        {
            glProfilerUpdate();
        }

        App.settings.getPrefs().flush();

        if (!buttons[devModeRow][devModeColumn].isChecked()
            && buttons[godModeRow][godModeColumn].isChecked())
        {
            buttons[godModeRow][godModeColumn].setChecked(false);
        }
    }

    private void updatePreferencesOnExit()
    {
        Trace.__FILE_FUNC();

        Developer.setDevMode(buttons[devModeRow][devModeColumn].isChecked());
        Developer.setGodMode(buttons[godModeRow][godModeColumn].isChecked() && Developer.isDevMode());
    }

    private void glProfilerUpdate()
    {
        if (Developer.isDevMode())
        {
            if (buttons[glProfilerRow][glProfilerColumn].isChecked())
            {
                // Profiling should be disabled on release software, hence
                // the warning suppression. Normally, not a good idea to
                // suppress such warnings but this if..else... is only
                // executed in Developer Mode.

                //noinspection LibGDXProfilingCode
                glProfiler.enable();
            }
            else
            {
                glProfiler.disable();
            }
        }
    }

    private void resetPreferencesToDefaults()
    {
        okToResetPrefs = true;

        App.settings.resetToDefaults();

        App.settings.getPrefs().putBoolean(Settings._SIGN_IN_STATUS, App.googleServices.isSignedIn());
        App.settings.getPrefs().putBoolean(Settings._ANDROID_ON_DESKTOP, AppConfig.isDesktopApp());

        App.settings.getPrefs().flush();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                boolean isChecked = App.settings.isEnabled(devMenu[row][column].prefName);

                buttons[row][column].setChecked(isChecked);
                buttons[row][column].setText(isChecked ? " ON" : " OFF");
            }
        }

        okToResetPrefs = false;
    }

    private void clearActors()
    {
        table.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
        exitButton.addAction(Actions.removeActor());
        buttonResetHiScores.addAction(Actions.removeActor());
        buttonResetPrefs.addAction(Actions.removeActor());
        buttonGLProfiler.addAction(Actions.removeActor());
        buttonResetStats.addAction(Actions.removeActor());
        buttonCollisionDump.addAction(Actions.removeActor());
        heading.addAction(Actions.removeActor());

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                buttons[row][column].addAction(Actions.removeActor());
            }
        }
    }

    // TODO: 14/04/2019 - Why is this called quickSetup() ???
    private void quickSetup()
    {
        for (int row = 0; row < devMenu.length; row++)
        {
            int length = devMenu[row].length;

            for (int column = 0; column < length; column++)
            {
                String prefName = devMenu[row][column].prefName;

                switch (prefName)
                {
                    case Settings._GL_PROFILER:
                        glProfilerColumn = column;
                        glProfilerRow = row;
                        break;
                    case _DEVMODE:
                        devModeColumn = column;
                        devModeRow = row;
                        break;
                    case _GODMODE:
                        godModeColumn = column;
                        godModeRow = row;
                        break;
                }
            }
        }
    }

    private void loadDevMenu()
    {
        DMEntry[][] devMenuDefaults =
            {
                {
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                    new DMEntry("", "")
                },
                {
                    new DMEntry("Scroll Demo", Settings._SCROLL_DEMO),
                    new DMEntry("Using Box2D", Settings._BOX2D_PHYSICS),
                    new DMEntry("Installed", Settings._INSTALLED)
                },
                {
                    new DMEntry("Sprite Boxes", Settings._SPRITE_BOXES),
                    new DMEntry("B2D Renderer", Settings._B2D_RENDERER),
                    new DMEntry("SHow Hints", Settings._SHOW_HINTS)
                },
                {
                    new DMEntry("Tile Boxes", Settings._TILE_BOXES),
                    new DMEntry("Use Ashley ECS", Settings._USING_ASHLEY_ECS),
                    new DMEntry("Vibrations", Settings._VIBRATIONS)
                },
                {
                    new DMEntry("Button Outlines", Settings._BUTTON_BOXES),
                    new DMEntry("Shader Program", Settings._SHADER_PROGRAM),
                    new DMEntry("Music Enabled", Settings._MUSIC_ENABLED),
                },
                {
                    new DMEntry("Show FPS", Settings._SHOW_FPS),
                    new DMEntry("GLProfiler", Settings._GL_PROFILER),
                    new DMEntry("FX Enabled", Settings._SOUNDS_ENABLED),
                },
                {
                    new DMEntry("Show Debug", Settings._SHOW_DEBUG),
                    new DMEntry("Enemies", Settings._ENABLE_ENEMIES),
                    new DMEntry("", ""),
                },
                {
                    new DMEntry("Android on Desktop", Settings._ANDROID_ON_DESKTOP),
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                },
                {
                    new DMEntry("Spawnpoints", Settings._SPAWNPOINTS),
                    new DMEntry("", ""),
                    new DMEntry("Play Services", Settings._PLAY_SERVICES)
                },
                {
                    new DMEntry("Menu Page Heaps", Settings._MENU_HEAPS),
                    new DMEntry("", ""),
                    new DMEntry("Achievements", Settings._ACHIEVEMENTS)
                },
                {
                    new DMEntry("Disable Menu Screen", Settings._DISABLE_MENU_SCREEN),
                    new DMEntry("", ""),
                    new DMEntry("Challenges", Settings._CHALLENGES)
                },
                {
                    new DMEntry("Cull Sprites", Settings._CULL_SPRITES),
                    new DMEntry("", ""),
                    new DMEntry("Signin Status", Settings._SIGN_IN_STATUS)
                },
                {
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                    },
                {
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                    new DMEntry("", ""),
                    },
                {
                    new DMEntry("Dev Mode", _DEVMODE),
                    new DMEntry("Invincibilty", _GODMODE),
                    new DMEntry("", ""),
                },
            };

        devMenu = new DMEntry[devMenuDefaults.length][_TABLE_COLUMNS];

        for (int row = 0; row < devMenuDefaults.length; row++)
        {
            //noinspection ManualArrayCopy
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                devMenu[row][column] = devMenuDefaults[row][column];
            }
        }
    }

    public void debugReport()
    {
        Trace.__FILE_FUNC_WithDivider();

        for (DMEntry[] entry : devMenu)
        {
            for (DMEntry dmEntry : entry)
            {
                if (!dmEntry.string.isEmpty())
                {
                    Trace.dbg(dmEntry.string + ": " + App.settings.getPrefs().getBoolean(dmEntry.prefName));
                }
            }
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();

        foreground.dispose();
        exitButton.clear();
        buttonResetPrefs.clear();
        buttonResetHiScores.clear();
        buttonResetStats.clear();
        buttonGLProfiler.clear();
        heading.clear();
        table.clear();
        scrollPane.clear();

        foreground          = null;
        exitButton          = null;
        buttonResetPrefs    = null;
        buttonResetHiScores = null;
        buttonResetStats    = null;
        buttonGLProfiler    = null;
        heading             = null;
        table               = null;
        scrollPane          = null;
    }
}
