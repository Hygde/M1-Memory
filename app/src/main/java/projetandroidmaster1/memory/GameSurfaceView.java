package projetandroidmaster1.memory;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    /** VIEW VARIABLES **/
    private Resources 	mRes;
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

    private Bitmap[] 	zone = new Bitmap[4];
    Icon[][]            truePanel = new Icon[5][4];                // our reftab used to place the icons.
    private Bitmap 		un;
    private Context 	mContext;
    ArrayList<Icon>     iconList = new ArrayList<Icon>();

    int panelTopAnchor;
    int panelLeftAnchor;
    static final int    panelHeight   = 5;
    static final int    panelWidth    = 4;


    /** INTERFACE **/
    private ProgressBar maxTryBar;
    private int         maxTryRef;
    private int         maxTryTemp;
    private TimeSpend   chrono;
    static final int    panelSquareSize = 280;

    /** MODEL VARIABLES **/
    private boolean     isTheGameRunning    = false;    // Is the player allowed to make interactions with the interface ?
    private boolean     win                 = false;
    private Icon        firstIconRevealed   = null;    // is one icon already revealed ?


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

        // initialisation des parmametres du jeu
        initparameters();

        // creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);
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

        // Launching the main thread
        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // dessin du gagne si gagn�
    private void paintwin(Canvas canvas) {
        //canvas.drawBitmap(win, carteLeftAnchor+ 3*carteTileSize, carteTopAnchor+ 4*carteTileSize, null);
    }

    // Drawing the Panel
    private void drawPanel(Canvas canvas, String mode) {
        for (int i=0; i< panelHeight ; i++) {
            for (int j=0; j< panelWidth; j++) {
                drawIcon(canvas, truePanel[i][j], i, j, mode);
            }
        }
    }

    // Draw a single bitmap icon for the tempPanel
    /* Existing modes :
        - show (at the beginning of the game)
        - hide
     */
    private void drawIcon(Canvas canvas, Icon icon, int i, int j, String mode) {
        // If we want to show the panel content OR if the icon is found OR revealed
        if (mode == "show") {
            switch (icon.getName()) {
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
            }
        }

        else if (mode == "hide") {
            if(icon.found || icon.revealed) {
                switch (icon.getName()) {
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
                }
            }
            else if (icon.isDiscovered()) {
                canvas.drawBitmap(hidden_discovered, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
            }
            else {
                canvas.drawBitmap(hidden_undiscovered, panelLeftAnchor + j * panelSquareSize, panelTopAnchor + i * panelSquareSize, null);
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

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {

        // When an icon is touched during the game : reveal it
        Canvas c = null;
        if (isTheGameRunning) {

            // Chrono is launch at first try
            if (maxTryTemp == maxTryRef) {
                chrono.startChrono();
            }

            int iconPosition[] = getIconPosition(event);
            int x = iconPosition[0];
            int y = iconPosition[1];
            if (y > 4) y = 4;
            //String icon = truePanel[y][x];
            //tempPanel[y][x] = icon;

            // In every case, revealing the icon
            truePanel[y][x].setDiscovered(true);
            truePanel[y][x].setRevealed(true);

            drawHiddenPanel(c);
            // No icon revealed
            if(firstIconRevealed == null) {
                firstIconRevealed = truePanel[y][x];
            }
            // if one icon is already waiting for a match
            else {
                this.maxTryTemp--;
                maxTryBar.setProgress(maxTryTemp);

                // is it a match ?
                if (firstIconRevealed.getName() == truePanel[y][x].getName()) {
                    firstIconRevealed.setFound(true);
                    truePanel[y][x].setFound(true);

                    // if all icons are found : the game is over
                    if(allFound(truePanel)) {
                        endingGame(true);
                    }
                }

                // if not, hiding the icons
                else {
                    if (maxTryTemp == 0) {
                        endingGame(false);
                    }

                    firstIconRevealed.setRevealed(false);
                    truePanel[y][x].setRevealed(false);
                    try {
                        cv_thread.sleep(1000);
                        drawHiddenPanel(c);
                    } catch(Exception e){}
                }
                firstIconRevealed = null;
            }
        }

        return super.onTouchEvent(event);
    }

    public void drawHiddenPanel(Canvas c) {
        // Hiding the panel and launching the game
        try {
            c = holder.lockCanvas(null);
            drawPanel(c, "hide");
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
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
                drawPanel(c, "show");
            }
            catch(Exception e) {}
            finally {
                holder.unlockCanvasAndPost(c);
            }
            cv_thread.sleep(2000);      // TODO : change to 5000
        }
        catch(Exception e) {}

        isTheGameRunning = true;
        drawHiddenPanel(c);
        debug_truePanelContent();

        // MAIN GAME LOOP
        /*while (isTheGameRunning) {
            try {
                // PAUSE
                //cv_thread.sleep(40); // on doit endormir le thread pour limiter le nombre d'images par seconde
                //currentStepZone = (currentStepZone + 1) % maxStepZone;
            } catch(Exception e) {
                // ERREUR
                // on entre dans le catch quand la boucle tente d'endormir le thread principal : l'erreur s'affiche puisqu'il n'existe plus. Solution : sortir de la boucle
                //Log.e("-> RUN <-", "PB DANS RUN");
                break;
            }
        }*/
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
        ArrayList<Icon> tempIconList = iconList;
        Collections.shuffle(tempIconList);

        for(int i = 0; i < truePanel.length ; i++) {
            for(int j = 0; j < truePanel[i].length ; j++) {
                // the list is shuffled : for each turn, we just treat it as a pile
                truePanel[i][j] = tempIconList.get(0);
                tempIconList.remove(0);
            }
        }
    }

    public boolean isWin() {
        return this.win;
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

    private boolean allFound(Icon[][] truePanel) {
        for (int i=0; i< truePanel.length ; i++) {
            for (int j=0; j< truePanel[i].length ; j++) {
                if(!truePanel[i][j].isFound())
                    return false;
            }
        }
        return true;
    }

    private void endingGame(boolean win) {
        this.win = win;
        Thread.currentThread().interrupt();
        cv_thread = null;
        ((Activity) mContext).finish();
    }

    public int getRemainingTries() {
        return this.maxTryTemp;
    }

    public void setMaxTry(ProgressBar bar, int value) {
        this.maxTryBar = bar;
        this.maxTryRef = this.maxTryTemp = value;
    }

    public void setChrono(ProgressBar bar, long time) {
        chrono = new TimeSpend(bar, time);
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
        Log.i("-> FCT <-", "surfaceDestroyed");
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
        Log.e("MEMORY : ", "GameSurfaceView.debug_panelContent():"+output);
    }

    /** SOKOBAN CODE **/
    // verification que nous sommes dans le tableau
    /*private boolean _IsOut(int x, int y) {
        if ((x < 0) || (x > carteWidth- 1)) {
            return true;
        }
        if ((y < 0) || (y > carteHeight- 1)) {
            return true;
        }
        return false;
    }*/

    //controle de la valeur d'une cellule
    /*private boolean IsCell(int x, int y, int mask) {
        if (carte[y][x] == mask) {
            return true;
        }
        return false;
    }*/
    /*public boolean isWinBitmapTouched(MotionEvent event) {
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
    }*/

    /*public void setLevel2() {
        refdiamants[0] = new int[]{3, 5};
        refdiamants[1] = new int[]{6, 6};
        refdiamants[2] = new int[]{4, 5};
        refdiamants[3] = new int[]{3, 7};
    }*/

    // Fills the temporary panel with hidden_undiscovered icons
    /*public void setTempPanel() {
        for(int i = 0; i < tempPanel.length ; i++) {
            for(int j = 0; j < tempPanel[i].length ; j++) {
                tempPanel[i][j] = "hidden_undiscovered";
            }
        }
        //debug_tempPanel();
    }*/

}
