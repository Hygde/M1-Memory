package projetandroidmaster1.memory;




import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MemoryView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	// Declaration des images
    private Bitmap 		block;
    private Bitmap[] 	zone = new Bitmap[4];
    private Bitmap 		win; 
    
	// Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;    
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    
    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte

    // taille de la carte
    static final int    carteWidth    = 10;
    static final int    carteHeight   = 10;
    static final int    carteTileSize = 20;

    // constante modelisant les differentes types de cases
    static final int    CST_block     = 0;
    static final int    CST_diamant   = 1;
    static final int    CST_perso     = 2;
    static final int    CST_zone      = 3;
    static final int    CST_vide      = 4;

    // tableau de reference du terrain
    int [][] ref    = {
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

    // position de reference des diamants
    int [][] refdiamants   = {
        {2, 3},
        {2, 6},
        {6, 3},
        {6, 6}
    };


    // position courante des diamants
    int [][] diamants   = {
            {2, 3},
            {2, 6},
            {6, 3},
            {6, 6}
        };


    /* compteur et max pour animer les zones d'arriv?e des diamants */
    int currentStepZone = 0;
    int maxStepZone     = 4;

    // thread utiliser pour animer les zones de depot des diamants
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
    public MemoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        
        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed        
    	holder = getHolder();
        holder.addCallback(this);    
        
        // chargement des images
        mContext	= context;
        mRes 		= mContext.getResources();        
        block 		= BitmapFactory.decodeResource(mRes, R.drawable.block);
        zone[0] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_01);
        zone[1] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_02);
        zone[2] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_03);
        zone[3] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_04);
    	win 		= BitmapFactory.decodeResource(mRes, R.drawable.win);
    	
    	// initialisation des parmametres du jeu
    	initparameters();

    	// creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true); 
        	
    	
    }    

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadBoard() {
    	for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                carte[j][i]= ref[j][i];
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
        carte           = new int[carteHeight][carteWidth];
        loadBoard();
        carteTopAnchor  = (getHeight()- carteHeight*carteTileSize)/2;
        carteLeftAnchor = (getWidth()- carteWidth*carteTileSize)/2;

        for (int i=0; i< 4; i++) {
            diamants[i][1] = refdiamants[i][1];
            diamants[i][0] = refdiamants[i][0];
        }
        if ((cv_thread!=null) && (!cv_thread.isAlive())) {        	
        	cv_thread.start();
        	Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // dessin du gagne si gagné
    private void paintwin(Canvas canvas) {
    	canvas.drawBitmap(win, carteLeftAnchor+ 3*carteTileSize, carteTopAnchor+ 4*carteTileSize, null);
    }    
    
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


    private boolean isWon() {
        return true;
    }
    
    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
		canvas.drawRGB(44,44,44);
    	if (isWon()) {
        	paintcarte(canvas);
        	paintwin(canvas);        	
        } else {
        	paintcarte(canvas);
            // todo : paint les autres éléments de l'interface
        }    	   	
        
    }
    
    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
    	initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
    	Log.i("-> FCT <-", "surfaceCreated");    	        
    }

    
    public void surfaceDestroyed(SurfaceHolder arg0) {
    	Log.i("-> FCT <-", "surfaceDestroyed");    	        
    }    

    /**
     * run (run du thread cree)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib?re le canvas
     */
    public void run() {
    	Canvas c = null;

        while (in) {
            try {
                // PAUSE
                cv_thread.sleep(40); // on doit endormir le thread pour limiter le nombre d'images par seconde
                currentStepZone = (currentStepZone + 1) % maxStepZone;
                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                	if (c != null) {
                		holder.unlockCanvasAndPost(c);
                    }
                }
            } catch(Exception e) {
                // ERREUR
                // on entre dans le catch quand la boucle tente d'endormir le thread principal : l'erreur s'affiche puisqu'il n'existe plus. Solution : sortir de la boucle
            	break;
                //Log.e("-> RUN <-", "PB DANS RUN");
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

        if (isWinBitmapTouched(event) && isWon()) {
            setLevel2();
            initparameters();
        }


        getTouchPosition(event);

        Log.i("-> FCT <-", "onTouchEvent: "+ event.getX());
    	if (event.getY()<50) {
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
    	} 
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

    private int[] getTouchPosition(MotionEvent event){
        int position[] = new int[2];
        int x = (int)event.getX();
        int y = (int)event.getY();


        if (
                (x > carteLeftAnchor && x < carteLeftAnchor + ref.length*carteTileSize ) &&
                        (y > carteTopAnchor && y < carteTopAnchor + ref[0].length*carteTileSize)){
                position[0] = (x/carteTileSize) - (carteLeftAnchor/carteTileSize);
                position[1] = (y/carteTileSize) - (carteTopAnchor/carteTileSize);
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


}
