package projetandroidmaster1.memory;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {



    // Declaration des images
    private Bitmap 		block;
    private Bitmap 		batman;
    private Bitmap 		firefox;
    private Bitmap 		hidden_discovered;
    private Bitmap 		hidden_undiscovered;
    private Bitmap 		holidays;
    private Bitmap 		kiss;
    private Bitmap 		mortal;
    private Bitmap 		msn;
    private Bitmap 		pig;
    private Bitmap 		pinky;
    private Bitmap 		puma;
    private Bitmap 		un;

    private Bitmap[] 	zone = new Bitmap[4];
    private Bitmap 		win;

    // Can the player make interactions with the interface ?
    private boolean     isTheGameRunning = false;

    // Declaration des objets Ressources et Context permettant d'acc�der aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // constantes modelisant les differentes types de cases
    ArrayList<Icon> iconList = new ArrayList<Icon>();

    // tableau de reference du jeu
    Icon[][] truePanel = new Icon[5][4];                // our reftab used to place the icons.
    //Icon[][] tempPanel = new String[5][4];
    //boolean[][] discoveredIcons = new boolean[5][4];    // using this table to know if the icons have been discovered
    int panelTopAnchor;
    int panelLeftAnchor;
    static final int    panelHeight   = 5;
    static final int    panelWidth    = 4;
    static final int    panelSquareSize = 280;



    /************* TODO : Sokoban code to remove *************/
    // tableau modelisant la carte du jeu
    int[][] carte;
    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonn�es en Y du point d'ancrage de notre carte

    int        carteLeftAnchor;                  // coordonn�es en X du point d'ancrage de notre carte
    // TODO : redefine the size of the map
    // taille de la carte
    static final int    carteWidth    = 10;
    static final int    carteHeight   = 10;
    static final int    carteTileSize = 20;

    // TODO : remove
    static final int    CST_block     = 0;
    static final int    CST_diamant   = 1;
    static final int    CST_perso     = 2;
    static final int    CST_zone      = 3;
    static final int    CST_vide      = 4;

    // TODO : remove (variables begenning with _ are old one, which we shall remove once ce project is done)
    int [][] _ref = {
            {CST_vide, CST_block, CST_block,CST_block, CST_block, CST_block, CST_block, CST_block, CST_block, CST_vide},
            {CST_block, CST_block, CST_block,CST_vide, CST_vide, CST_vide, CST_vide, CST_block, CST_block, CST_block},
            {CST_block, CST_vide, CST_vide,CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_block},
            {CST_block, CST_vide, CST_vide, CST_block, CST_vide, CST_vide, CST_block, CST_vide, CST_vide, CST_block},
            {CST_block, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_block},
            {CST_block, CST_vide, CST_block, CST_vide, CST_vide, CST_vide, CST_vide, CST_block, CST_vide, CST_block},
            {CST_block, CST_vide, CST_vide, CST_vide, CST_block, CST_block, CST_vide, CST_vide, CST_vide, CST_block},
            {CST_block, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_block},
            {CST_block, CST_block, CST_vide,CST_zone, CST_zone, CST_zone, CST_zone, CST_vide, CST_block, CST_block},
            {CST_vide, CST_block, CST_block,CST_block, CST_block, CST_block, CST_block, CST_block, CST_block, CST_vide}
    };

    // TODO : remove
    // position de reference des diamants
    int [][] refdiamants   = {
            {2, 3},
            {2, 6},
            {6, 3},
            {6, 6}
    };

    // TODO : remove
    // position courante des diamants
    int [][] diamants   = {
            {2, 3},
            {2, 6},
            {6, 3},
            {6, 6}
    };

    // TODO : remove
    /* compteur et max pour animer les zones d'arriv?e des diamants */
    int currentStepZone = 0;
    int maxStepZone     = 4;

    // thread utilisé pour animer les zones de depot des diamants
    private     boolean in      = true;
    private     Thread  cv_thread;
    SurfaceHolder holder;

    Paint paint;

    /**
     * The constructor called from the main JetBoy activity
     *
     * @param context
     * @param attrs
     */
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext	= context;
        mRes 		= mContext.getResources();
        batman      = BitmapFactory.decodeResource(mRes, R.drawable.icon_batman);
        firefox     = BitmapFactory.decodeResource(mRes, R.drawable.icon_firefox);
        holidays    = BitmapFactory.decodeResource(mRes, R.drawable.icon_holidays);
        kiss        = BitmapFactory.decodeResource(mRes, R.drawable.icon_kiss);
        mortal      = BitmapFactory.decodeResource(mRes, R.drawable.icon_mortal);
        msn         = BitmapFactory.decodeResource(mRes, R.drawable.icon_msn);
        pig         = BitmapFactory.decodeResource(mRes, R.drawable.icon_pig);
        pinky       = BitmapFactory.decodeResource(mRes, R.drawable.icon_pinky);
        puma        = BitmapFactory.decodeResource(mRes, R.drawable.icon_puma);
        un          = BitmapFactory.decodeResource(mRes, R.drawable.icon_un);
        hidden_discovered = BitmapFactory.decodeResource(mRes, R.drawable.icon_hidden_discovered);
        hidden_undiscovered = BitmapFactory.decodeResource(mRes, R.drawable.icon_hidden_uncovered);

        //** TODO : remove below
        block 		= BitmapFactory.decodeResource(mRes, R.drawable.block);
        win 		= BitmapFactory.decodeResource(mRes, R.drawable.win);
        //** TODO : remove above

        // initialisation des parmametres du jeu
        initparameters();

        // creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);
    }



    // chargement du niveau a partir du tableau de reference du niveau
    private void _loadBoard() {
        for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                carte[j][i]= _ref[j][i];
            }
        }
    }

    // initialisation du jeu
    public void initparameters() {
        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        // Setting up the virtual truePanel
        setIconList();
        setTruePanel();
        //setTempPanel();
        panelTopAnchor  = (getHeight()- panelHeight* panelSquareSize)/2;
        panelLeftAnchor = (getWidth()- panelWidth* panelSquareSize)/2;

        // TODO : remove code below
        carte           = new int[carteHeight][carteWidth];
        _loadBoard();
        carteTopAnchor  = (getHeight()- carteHeight*carteTileSize)/2;
        carteLeftAnchor = (getWidth()- carteWidth*carteTileSize)/2;
        for (int i=0; i< 4; i++) {
            diamants[i][1] = refdiamants[i][1];
            diamants[i][0] = refdiamants[i][0];
        }
        // TODO : remove code above

        // Launching the main thread
        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // dessin du gagne si gagn�
    private void paintwin(Canvas canvas) {
        canvas.drawBitmap(win, carteLeftAnchor+ 3*carteTileSize, carteTopAnchor+ 4*carteTileSize, null);
    }

    // Drawing the truePanel
    private void drawTruePanel(Canvas canvas) {
        for (int i=0; i< panelHeight ; i++) {
            for (int j=0; j< panelWidth; j++) {
                drawOneIcon(canvas, truePanel[i][j].getName(), i, j);
            }
        }
    }

    // Render the panel displayed during the game
    public void drawTempPanel(Canvas canvas) {
        for(int i = 0 ; i < panelHeight; i++) {
            for (int j = 0; j < panelWidth ; j++) {
                // TODO : add a condition : if icon != "hidden_undiscovered", draw "hidden_uncovered"
                //drawOneIcon(canvas, tempPanel[i][j], i, j);
            }
        }
    }

    // Draw a single bitmap icon
    private void drawOneIcon(Canvas canvas, String icon, int i, int j) {
        switch (icon) {
            case "batman":
                canvas.drawBitmap(batman, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "firefox":
                canvas.drawBitmap(firefox, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "holidays":
                canvas.drawBitmap(holidays, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "kiss":
                canvas.drawBitmap(kiss, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "mortal":
                canvas.drawBitmap(mortal, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "msn":
                canvas.drawBitmap(msn, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "pig":
                canvas.drawBitmap(pig, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "pinky":
                canvas.drawBitmap(pinky, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "puma":
                canvas.drawBitmap(puma, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "un":
                canvas.drawBitmap(un, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "hidden_undiscovered":
                canvas.drawBitmap(hidden_undiscovered, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
            case "hidden_discovered":
                canvas.drawBitmap(hidden_undiscovered, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
                break;
        }

    }

    private void revealFirstIcon(Canvas canvas, String icon, int x, int y) {
        // Reveal the icon
        /*try {
            try {
                canvas = holder.lockCanvas(null);
                for (int i = 0 ; i < tempPanel.length ; i++) {
                    for (int j = 0 ; j < tempPanel[0].length ; j++) {
                        drawOneIcon(
                                canvas,
                                tempPanel[i][j],
                                i,
                                j
                        );
                    }
                }
            }
            catch(Exception e) {}
            finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
        catch(Exception e) {}*/
    }


    // filling the iconList with all the names
    public void setIconList() {
        this.iconList.add(new Icon("batman", 1));
        this.iconList.add(new Icon("batman", 2));
        this.iconList.add(new Icon("firefox", 1));
        this.iconList.add(new Icon("firefox", 2));
        this.iconList.add(new Icon("holidays", 1));
        this.iconList.add(new Icon("holidays", 2));
        this.iconList.add(new Icon("kiss", 1));
        this.iconList.add(new Icon("kiss", 2));
        this.iconList.add(new Icon("mortal", 1));
        this.iconList.add(new Icon("mortal", 2));
        this.iconList.add(new Icon("msn", 1));
        this.iconList.add(new Icon("msn", 2));
        this.iconList.add(new Icon("pig", 1));
        this.iconList.add(new Icon("pig", 2));
        this.iconList.add(new Icon("pinky", 1));
        this.iconList.add(new Icon("pinky", 2));
        this.iconList.add(new Icon("puma", 1));
        this.iconList.add(new Icon("puma", 2));
        this.iconList.add(new Icon("un", 1));
        this.iconList.add(new Icon("un", 2));
    }

    // Filling the truePanel with random icons
    public void setTruePanel() {

        // we need to duplicate 2 identic lists to represent the pairs
        ArrayList<Icon> randomList = iconList;
        randomList.addAll(iconList);
        Collections.shuffle(randomList);

        for(int i = 0; i < truePanel.length ; i++) {
            for(int j = 0; j < truePanel[i].length ; j++) {
                // the list is shuffled : for each turn, we just treat it as a pile
                truePanel[i][j] = randomList.get(0);
                randomList.remove(0);
            }
        }
    }

    // Fills the temporary panel with hidden_undiscovered icons
    /*public void setTempPanel() {
        for(int i = 0; i < tempPanel.length ; i++) {
            for(int j = 0; j < tempPanel[i].length ; j++) {
                tempPanel[i][j] = "hidden_undiscovered";
            }
        }
        //debug_tempPanel();
    }*/

    //** TODO : remove code below
    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                switch (carte[i][j]) {
                    case CST_block:
                        canvas.drawBitmap(block, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case CST_zone:
                        canvas.drawBitmap(zone[currentStepZone],carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                }
            }
        }
    }
    //** TODO : remove code abode

    private boolean isWon() {
        return true;
    }

    // Drawing the game
    private void _nDraw(Canvas canvas) {
        //canvas.drawRGB(44,44,44);
        drawTruePanel(canvas);

        /*if (isWon()) {
            paintcarte(canvas);
            paintwin(canvas);
        } else {
            //paintcarte(canvas);
            // todo : paint les autres �l�ments de l'interface
        }*/

    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        //Log.i("-> FCT <-", "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        //Log.i("-> FCT <-", "surfaceDestroyed");
    }

    /**
     * run (run du thread cree)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib?re le canvas
     */
    public void run() {
        Canvas c = null;

        // The truePanel is revealed during 5 seconds, then the images hide
        try {
            try {
                c = holder.lockCanvas(null);
                drawTruePanel(c);
            }
            catch(Exception e) {}
            finally {
                holder.unlockCanvasAndPost(c);
            }
            cv_thread.sleep(2000);      // TODO : change to 5000
        }
        catch(Exception e) {}

        // Hiding the panel and launching the game
        try {
            c = holder.lockCanvas(null);
            drawTempPanel(c);
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
        isTheGameRunning = true;

        // MAIN GAME LOOP
        while (in) {
            try {
                // PAUSE
                cv_thread.sleep(40); // on doit endormir le thread pour limiter le nombre d'images par seconde
                //currentStepZone = (currentStepZone + 1) % maxStepZone;
            } catch(Exception e) {
                // ERREUR
                // on entre dans le catch quand la boucle tente d'endormir le thread principal : l'erreur s'affiche puisqu'il n'existe plus. Solution : sortir de la boucle
                //Log.e("-> RUN <-", "PB DANS RUN");
                break;
            }
        }
    }

    // verification que nous sommes dans le tableau
    private boolean IsOut(int x, int y) {
        if ((x < 0) || (x > carteWidth- 1)) {
            return true;
        }
        if ((y < 0) || (y > carteHeight- 1)) {
            return true;
        }
        return false;
    }

    //controle de la valeur d'une cellule
    private boolean IsCell(int x, int y, int mask) {
        if (carte[y][x] == mask) {
            return true;
        }
        return false;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
        boolean firstIconRevealed = false;      // to know if we already revealed one icon

        // TODO : remove
        /*if (isWinBitmapTouched(event) && isWon()) {
            setLevel2();
            initparameters();
        }*/

        // When an icon is touched during the game : reveal it
        if (isTheGameRunning) {
            Canvas c = null;
            int iconPosition[] = getIconPosition(event);
            int x = iconPosition[0];
            int y = iconPosition[1];
            if (y > 4) y = 4;
            //String icon = truePanel[y][x];
            //tempPanel[y][x] = icon;

            if(!firstIconRevealed) {
                revealFirstIcon(c, truePanel[y][x].name, y, x);
                firstIconRevealed = true;
            }
            // if one icon is already waiting for a match
            else {
               //revealSecondIcond(c, icon, y, x);
               firstIconRevealed = false;
            }
        }


        // TODO : remove
        //Log.i("-> FCT <-", "onTouchEvent: "+ event.getX());
        /*if (event.getY()<50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
        } else if (event.getY()>getHeight()-50) {
            if (event.getX()>getWidth()-50) {
                onKeyDown(KeyEvent.KEYCODE_0, null);
            } else {
                onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null);
            }
        } else if (event.getX()<50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
        } else if (event.getX()>getWidth()-50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }*/
        return super.onTouchEvent(event);
    }

    public boolean isWinBitmapTouched(MotionEvent event) {
        // Launching the second level when touching the "win" Bitmap
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            int carteLeftBoard = carteLeftAnchor + 3*carteTileSize;
            int carteRightBoard = carteLeftAnchor + 3*carteTileSize + 80;
            int carteBottomBoard = carteTopAnchor + 4 * carteTileSize;
            int carteTopBoard = carteTopAnchor + 4 * carteTileSize + 40;

            if((x >= carteLeftBoard && x <= carteRightBoard) && (y >= carteBottomBoard && y <= carteTopBoard)) {
                return true;
            }
            //Log.i("=== TESTY TOUCH", "Touch coordinates : " +
            //      String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
        }
        return false;
    }

    private int[] getIconPosition(MotionEvent event){
        int position[] = new int[2];
        int x = (int)event.getX();
        int y = (int)event.getY();

        if (
                (x > panelLeftAnchor && x < panelLeftAnchor + truePanel[0].length*panelSquareSize ) &&
                        (y > panelTopAnchor && y < panelTopAnchor + truePanel.length*panelSquareSize)){
            position[0] = (x/panelSquareSize) - (panelLeftAnchor/panelSquareSize);
            position[1] = (y/panelSquareSize) - (panelTopAnchor/panelSquareSize);
        }
        Log.i("====> Position :", "x= "+Integer.toString(position[0])+" y= "+Integer.toString(position[1]));
        return position;
    }

    public void setLevel2() {
        refdiamants[0] = new int[]{3, 5};
        refdiamants[1] = new int[]{6, 6};
        refdiamants[2] = new int[]{4, 5};
        refdiamants[3] = new int[]{3, 7};
    }


    /** DEBUG FUNCTIONS **/

    public void debug_toast(String input) {
        //Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(mContext, text, duration);
        toast.show();
    }


    public void debug_truePanelContent() {
        String output = "--- BEGIN ---\n";
        for (int i = 0; i < truePanel.length ; i++) {
            output += "\n-- ROW "+i+" --\n";
                for (int j = 0; j < truePanel[i].length ; j++) {
                    output+=" -"+ truePanel[i][j].getName()+"- ";
                }
        }
        Log.e("MEMORY : ", "GameSurfaceView.debug_panelCOntent():"+output);
    }

    /*public void debug_tempPanel() {
        String output = "TEMPTAB\n";
        for (int i = 0 ; i < tempPanel.length ; i++) {
            output += "\n==ROW "+i+"\n";
            for (int j = 0 ; j < tempPanel[i].length ; j++) {
                output += "===ELEM : "+tempPanel[i][j]+" / ";
            }
        }
        Log.e("MEMORY : ", "GameSurfaceView.debug_tempPanel() : "+output);
    }*/


}
