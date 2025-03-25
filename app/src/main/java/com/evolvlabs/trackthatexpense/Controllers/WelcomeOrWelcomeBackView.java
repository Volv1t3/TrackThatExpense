package com.evolvlabs.trackthatexpense.Controllers;

import android.content.res.ColorStateList;
import android.content.res.loader.ResourcesLoader;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ApplicationDataPOJO;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ExpenseCategory;
import com.evolvlabs.trackthatexpense.R;
import com.google.android.material.button.MaterialButton;
import androidx.drawerlayout.widget.DrawerLayout;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo incluye la implemetacion del controlador para el fragment
 * visual de la bienvenida a la aplicacion. En este fragment nos encargamos de muy poco, dado que
 * es un componente puramente visual al que solo se le carga informacion dependiendo del estado
 * de la aplicacion.
 */
public class WelcomeOrWelcomeBackView extends Fragment {

    private boolean isThereAnyInformationToDisplay = false;
    private ScrollView scrollViewFromView;
    private LinearLayout welcomeOrWelcomeBackInternalLinearLayoutForProgressBars;
    private TextView welcomeOrWelcomeBackViewHeadingTextView;
    private TextView welcomeOrWelcomeBackViewCurrentGoalsTextView;

    /**
     * <body style="color:white">
     * Este metodo se encarga de crear y configurar la jerarquia de vistas asociada con este
     * fragmento al momento de ser creado. Sobrescribe el metodo base proporcionado por la clase
     * {@link Fragment}.
     * <br><br>
     * <strong>Descripcion del funcionamiento:</strong><br>
     * - Se valida si existe informacion cargada en la aplicacion al llamar al metodo
     * {@link #validateInformationLoaded()}.<br> - Luego, se utiliza el objeto
     * {@link LayoutInflater} para inflar el archivo de diseño XML correspondiente a este fragmento,
     * lo que genera la vista principal que sera mostrada al usuario.
     * </body>
     *
     * @param inflater           El {@link LayoutInflater} usado para inflar la vista del fragmento
     *                           desde XML.
     * @param container          El {@link ViewGroup} contenedor padre al cual se asociara la vista
     *                           inflada.
     * @param savedInstanceState Si no es null, contiene el estado previamente guardado del
     *                           fragmento.
     * @return La vista creada e inflada desde el recurso de diseño correspondiente.
     * @throws NullPointerException Si no se puede encontrar el recurso de diseño para el
     *                              fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        validateInformationLoaded();
        return inflater.inflate(R.layout.welcome_or_welcome_back_view,
                                container,
                                false);
    }

    /**
     * <body style="color:white">
     * Este metodo valida si existe informacion cargada en la aplicacion para determinar el estado
     * visual del componente. De manera interna, utiliza la instancia singleton de
     * {@link ApplicationDataPOJO} para verificar la existencia y contenido de las listas de
     * categorias de gastos y el mapa de gastos de la aplicacion.
     * <br><br>
     * <strong>Descripcion del funcionamiento:</strong><br>
     * - Si la instancia de {@link ApplicationDataPOJO} es nula, no hay datos cargados y se asigna
     * el estado {@code false} a la variable {@code isThereAnyInformationToDisplay}.<br> - Si tanto
     * las categorias como el mapa de gastos no estan vacios, se asigna el estado {@code true}. En
     * cualquier otro caso, el estado sera {@code false}.
     * <br>
     * <strong>Conceptos utilizados:</strong><br>
     * Este metodo ilustra el uso del patron de dise�o Singleton con {@link ApplicationDataPOJO} y
     * operaciones basicas de listas y mapas para gestionar el estado de la aplicacion.
     * </body>
     */
    private void validateInformationLoaded() {
        if (ApplicationDataPOJO.getApplicationDataPOJOInstance() != null) {
            if (!ApplicationDataPOJO
                    .getApplicationDataPOJOInstance()
                    .getApplicationDataPojoExpenseCategories().isEmpty()
                    &&
                    !ApplicationDataPOJO
                            .getApplicationDataPOJOInstance()
                            .getApplicationDataPojoExpensesMap().isEmpty()) {
                isThereAnyInformationToDisplay = true;
            } else {
                isThereAnyInformationToDisplay = false;
            }
        }
    }

    /**
     * <body style="color:white">
     * Este metodo se encarga de gestionar las acciones iniciales realizadas despues de que la vista
     * del fragmento haya sido creada. Aprovecha las caracteristicas del framework para configurar
     * la navegacion del menu e inicializa la interfaz de usuario de acuerdo con el estado actual de
     * la aplicacion.
     * <br><br>
     * <strong>Descripcion del funcionamiento:</strong><br>
     * - Configura el boton del menu de navegacion para abrir el panel correspondiente mediante el
     * uso de {@link DrawerLayout}.<br> - Valida si existe informacion cargada en la aplicacion
     * llamando al metodo {@link #validateInformationLoaded()}.<br> - Llama al metodo
     * {@link #modifyUIBasedOnContentAvailability()} para actualizar la interfaz del usuario de
     * acuerdo con el estado de la informacion disponible.<br>
     * <strong>Conceptos utilizados:</strong><br>
     * Uso integrado de patrones de diseño como el Singleton a traves de
     * {@link ApplicationDataPOJO}, manipulacion de navegacion con {@link DrawerLayout}, y manejo de
     * vistas Android para estructurar la interfaz del usuario.
     * </body>
     *
     * @param view               La {@link View} asociada con este fragmento, generada tras inflar
     *                           el archivo XML de diseño correspondiente.
     * @param savedInstanceState Si no es null, contiene el estado previamente guardado del
     *                           fragmento.
     * @throws NullPointerException Si algun componente esencial de la interfaz de usuario no puede
     *                              ser encontrado en el archivo de diseño correspondiente.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*La parte mas importante de esta seccion, es permitir la navegacion entre instancias,
        por lo que iniciamos conectado el boton que tiene el icono del menu con un listener al
        presionar que nos permite conectarnos con el drawer layout (un componente que permite
        mostrar un menu de navegacion) y establecer el comportamiento de aparicion del componente.*/
        MaterialButton menuButton = view.findViewById(R.id.menuButtonForNavigation);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        this.welcomeOrWelcomeBackViewHeadingTextView = (TextView) view.findViewById(
                R.id.welcomeOrWelcomeBackViewHeadingTextView);
        this.welcomeOrWelcomeBackViewCurrentGoalsTextView = (TextView) view.findViewById(
                R.id.welcomeOrWelcomeBackViewCurrentGoalsTextView);
        /*
         * Una vez realizada la carga del menu, es momento de cargar la informacion del usuario,
         * utilizamos el estado interno para mostrar o no la vista de nuestra aplicacion, con o
         * sin informacion
        */
        validateInformationLoaded();
        modifyUIBasedOnContentAvailability();


    }

    /**
     * <body style="color:white">
     * Este metodo se encarga de modificar dinamicamente la interfaz de usuario (UI) del fragmento
     * basado en la disponibilidad de informacion cargada en la aplicacion. Utiliza el estado
     * interno {@code isThereAnyInformationToDisplay} para decidir si mostrar datos del usuario o
     * una interfaz informativa indicando la ausencia de informacion.
     * <br><br>
     * <strong>Descripcion del funcionamiento:</strong><br>
     * - Valida si hay informacion usando {@link #validateInformationLoaded()}.<br> - Si hay
     * informacion disponible, se genera una UI detallada que incluye barras de progreso, categorias
     * de gastos y montos totales.<br> - Si no hay informacion, muestra una UI con mensajes de
     * bienvenida y un grafico informativo.<br>
     * <br>
     * <strong>Conceptos utilizados:</strong><br>
     * - Manipulacion dinamica de vistas Android: se crean programaticamente componentes como
     * {@link TextView}, {@link ProgressBar}, y {@link Space}.<br> - Colores e interacciones del
     * usuario ilustran estados visuales como superacion de limites en barras de progreso mediante
     * {@link ColorStateList} y {@link PorterDuff}.<br> - Uso de datos de un patron de diseno
     * Singleton atraves de {@link ApplicationDataPOJO} para acceder a categorias y gastos.
     * </body>
     */
    private void modifyUIBasedOnContentAvailability() {
        validateInformationLoaded();
        if (this.isThereAnyInformationToDisplay) {
            /*Si entramos aqui, existe aplicacion cargada dentro de nuestras listas, lo que nos
            permitiria mostrarla, por tanto, modificamos todos los parametros de la UI para
            darnos el gusto de poner la informacion correcta!*/
            //? Modificamos programaticamente el heading de la aplicacion
            try {
                TextView textView =
                        (TextView) this.getView().findViewById(
                                R.id.welcomeOrWelcomeBackViewHeadingTextView);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                textView.setGravity(Gravity.TOP);
                textView.setText(R.string.appWelcomeBackMessage);
                textView.setWidth(0);
                textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                //? Modificamos programaticamente el banner de nuestra aplicacion
                textView = (TextView)
                        this.getView().findViewById(R.id.welcomeOrWelcomeBackViewCurrentGoalsTextView);
                textView.setTextSize(30);
                textView.setText(R.string.appYourGoalsMessage);
            } catch (NullPointerException e) {
                Log.e("WelcomeOrWelcomeBackView", "Error al modificar la UI de la aplicacion: " +
                        "componente no encontrado" + e.getMessage());
            }

            try {
                this.scrollViewFromView = (ScrollView) this
                        .getView()
                        .findViewById(R.id.welcomeOrWelcomeBackViewScrollViewForInfo);

                //? 1. Para iniciar el proceso, tenemos que conseguir el LinearLayout que tiene
                // nuestro ScrollView dentro para poder manejar su contenido directamente,
                // hacemos esto para tener un layout vertical y no un cumulo de entradas en el
                // scrollview directamente.
                LinearLayout containerLayout = (LinearLayout) this.scrollViewFromView.getChildAt(0);

                //? LLamamos para obtener todas las categorias registradas en la aplicacion
                List<ExpenseCategory> expenseCategories = ApplicationDataPOJO
                        .getApplicationDataPOJOInstance()
                        .getApplicationDataPojoExpenseCategories();

                //? Iteramos sobre las categorias, la idea es que tenemos que generar componentes
                // por cada una de estas con un formato similar al de los campos de entrada de datos
                for (ExpenseCategory category : expenseCategories) {
                    // 2. Generamos el componente de texto, de esta forma colocamos al layout
                    // para que el texto use t odo el espacio del layout, y la altura solo la del
                    // texto.
                    TextView textView = new TextView(this.getContext());
                    textView.setTextSize(16);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    textView.setText(category.getExpenseCategoryName());
                    containerLayout.addView(textView);

                    //? 3. Anadimos el primer espacio para separar el layout y darle un poco de
                    // breathing room entre el texto y el progress bar
                    Space space1 = new Space(this.getContext());
                    space1.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    10,
                                    getResources().getDisplayMetrics()
                                                           )
                    ));
                    containerLayout.addView(space1);

                    //?4.  Generamos programaticamente la barra de progreso por la categoria, el
                    // objetvio es generar una barra simple, horizontal que presente en su total
                    // el valor maximo del mes definido por el usuario, y en su progress el total
                    // de los expenses de esa categoria
                    ProgressBar progressBar = new ProgressBar(
                            this.getContext(),
                            null,
                            android.R.attr.progressBarStyleHorizontal
                    );

                    progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    progressBar.setMinimumHeight(200);
                    //? Definimos el valor maximo de la barra de progreso con el valor definido
                    // por el usuario para el maximo de ese mes
                    BigDecimal maxValue = category.getExpenseCategoryMaxPerMonthValue();

                    //? Cargamos el progreso con el total de la categoria que tenemos en la
                    // aplicacion. Para manejar esto, si el total es mayor que el maximo del mes
                    // (que puede darse) marcamos en rojo la barra completamete
                    BigDecimal totalExpenses = ApplicationDataPOJO
                            .getApplicationDataPOJOInstance()
                            .getTotalPerCategory(category);

                    progressBar.setMax(maxValue.multiply(new BigDecimal("100")).intValue());
                    progressBar.setProgress(totalExpenses.multiply(new BigDecimal("100")).intValue());

                    if (totalExpenses.compareTo(maxValue) > 0) {
                        /*Si entramos en esta seccion, el usuario ha ingresado muchos gastos
                        comparados con el total que tiene para el mes, por tanto dibujamos la
                        barra con otro color. Para esto usamos los metodos del progress directamente
                        . */
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                        progressBar.setProgressBackgroundTintList(ColorStateList
                                                                          .valueOf(
                                                                          Color
                                                                          .parseColor("#B8E1CB")));
                        progressBar.setProgressBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                    } else {
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(
                                "#5D737E")));
                        progressBar.setProgressBackgroundTintList(ColorStateList
                                                                          .valueOf(
                                                                          Color
                                                                          .parseColor("#B8E1CB")));
                        progressBar.setProgressBackgroundTintMode(PorterDuff.Mode.DST_ATOP);
                    }

                    //? 4.1 Antes de ingresar la barra, creamos un componente que nos permita
                    // ingresar el total actual y el maximo para la categoria en la vista, un
                    // complemento informativo de la UI
                    LinearLayout amountsLayout = new LinearLayout(this.getContext());
                    amountsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    amountsLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView spentAmountView = new TextView(this.getContext());
                    LinearLayout.LayoutParams spentParams = new LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                    );
                    spentAmountView.setLayoutParams(spentParams);
                    spentAmountView.setText(String.format("$%.2f", totalExpenses.doubleValue()));
                    spentAmountView.setGravity(Gravity.START);

                    TextView totalAmountView = new TextView(this.getContext());
                    LinearLayout.LayoutParams totalParams = new LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                    );
                    totalAmountView.setLayoutParams(totalParams);
                    totalAmountView.setText(String.format("$%.2f", maxValue.doubleValue()));
                    totalAmountView.setGravity(Gravity.END);

                    amountsLayout.addView(spentAmountView);
                    amountsLayout.addView(totalAmountView);
                    containerLayout.addView(amountsLayout);


                    containerLayout.addView(progressBar);

                    //? 5. Cagamos un nuevo espacio y terminamos la carga para esta iteracion del
                    // loop
                    Space space2 = new Space(this.getContext());
                    space2.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    10,
                                    getResources().getDisplayMetrics()
                                                           )
                    ));
                    containerLayout.addView(space2);
                }
            } catch (Exception e) {
                Log.e("WelcomeOrWelcomeBackView", "Error al modificar la UI de la aplicacion: "
                        + e.getMessage());
            }
        }
        else {
            this.welcomeOrWelcomeBackViewHeadingTextView.setText(R.string.appWelcomeMessage);
            this.welcomeOrWelcomeBackViewCurrentGoalsTextView.setText(R.string.appNoGoalsMessage);

            this.welcomeOrWelcomeBackInternalLinearLayoutForProgressBars = (LinearLayout) this
                    .getView()
                    .findViewById(R.id.welcomeOrWelcomeBackInternalLinearLayoutForProgressBars);
            this.welcomeOrWelcomeBackInternalLinearLayoutForProgressBars.removeAllViews();
            this.welcomeOrWelcomeBackInternalLinearLayoutForProgressBars.setGravity(Gravity.CENTER_VERTICAL);

            ImageView informativeGraphic = new ImageView(this.getContext());

            // Layout parameters with specific width/height in dp
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    dpToPx(300),  // width 300dp
                    dpToPx(300)   // height 300dp
            );
            informativeGraphic.setBackground(ResourcesCompat.getDrawable(getResources(),
                                                                         R.drawable.rounded_corners_for_constrained_layout, null));
            imageParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL | Gravity.END;
            informativeGraphic.setLayoutParams(imageParams);

            informativeGraphic.setImageResource(R.drawable.no_data_vector_graphic);
            informativeGraphic.setScaleType(ImageView.ScaleType.FIT_CENTER);

            informativeGraphic.setMinimumWidth(dpToPx(463));
            informativeGraphic.setMinimumHeight(dpToPx(463));
            informativeGraphic.setMaxWidth(dpToPx(463));
            informativeGraphic.setMaxHeight(dpToPx(463));

            informativeGraphic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FFFFFFF")));
            informativeGraphic.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

            this.welcomeOrWelcomeBackInternalLinearLayoutForProgressBars.addView(informativeGraphic);

        }
    }


    /**
     * <body style="color:white">
     * Este metodo convierte un valor dado en densidad de pixeles (dp) a pixeles (px) dependiendo de
     * la densidad de pantalla actual del dispositivo. Usando las herramientas de Android como
     * {@link TypedValue} se asegura de manejar la diferencia entre dispositivos de diferentes
     * resoluciones o tamaños de pantalla.
     * <br><br>
     * <strong>Descripcion del funcionamiento:</strong><br>
     * - Utiliza {@link TypedValue#applyDimension} para calcular el equivalente en pixeles del valor
     * dp dado.<br> - Utiliza {@link android.util.DisplayMetrics} para obtener la escala de la
     * pantalla del dispositivo actual.<br> - Retorna un valor entero que representa la cantidad de
     * pixeles calculados.
     * <br>
     * <strong>Conceptos utilizados:</strong><br>
     * Conversión de unidades para adaptarse a pantallas variadas en los dispositivos Android.
     *
     * @param dp La cantidad de densidad de pixeles a convertir.
     * @return El equivalente en pixeles de la cantidad de dp ingresada.
     * @throws NullPointerException Si {@link #getResources()} retorna null en algun caso extremo.
     *                              </body>
     */
    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
                                              );
    }


    @Override
    public void onResume() {
        validateInformationLoaded();
        super.onResume();
        modifyUIBasedOnContentAvailability();
        //? Una vez que se vuelva a cargar la UI tenemos que recargar las listas.
        this.scrollViewFromView =
                (ScrollView) this.getView().findViewById(R.id.welcomeOrWelcomeBackViewScrollViewForInfo);
        LinearLayout containerLayout = (LinearLayout) this.scrollViewFromView.getChildAt(0);
        containerLayout.removeAllViews();
        modifyUIBasedOnContentAvailability();
    }
}
