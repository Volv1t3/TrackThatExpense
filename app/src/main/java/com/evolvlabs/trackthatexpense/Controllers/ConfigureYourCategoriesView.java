package com.evolvlabs.trackthatexpense.Controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ApplicationDataPOJO;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ExpenseCategory;
import com.evolvlabs.trackthatexpense.R;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author : Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo implementa el controlador total para el fragmento
 * configure_your_categories_view.xml, el cual incluye la funcionalidad de  eliminar categorias,
 * eliminar gastos por categoria o crear nuevas categorias
 */
public class ConfigureYourCategoriesView extends Fragment {

    private ScrollView     configureYourCategoriesScrollViewForCategoriesLabel;
    private MaterialButton configureYourCategoriesViewAddCategoryMaterialButton;
    private LinearLayout  configureYourCategoriesLinearLayoutForCategoriesLabels;


    /**
     * Configura y genera la vista asociada al fragmento. Permite inflar y crear la jerarquia de
     * interfaz definida en el archivo de diseño XML relacionado con este fragmento.
     *
     * @param inflater           El {@link LayoutInflater} utilizado para inflar la vista de este
     *                           fragmento.
     * @param container          El contenedor (de tipo {@link ViewGroup}) al que se añadira la
     *                           vista de este fragmento. Puede ser null si el fragmento no está
     *                           asociado a un contenedor.
     * @param savedInstanceState Una instancia de {@link Bundle} que contiene el estado previamente
     *                           guardado del fragmento, si existe. Si no hay estado guardado, sera
     *                           null.
     * @return Devuelve la vista ({@link View}) creada por el metodo {@code inflate}, asociada a
     * este fragmento.
     * @throws NullPointerException Si el archivo de diseño especificado no existe en el proyecto,
     *                              este metodo lanzara una excepcion {@link NullPointerException}.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configure_your_categories_view, 
                                container, false);
    }

    /**
     * Invocado cuando la vista asociada al fragmento ha sido creada. Este metodo configura los
     * elementos de la interfaz de usuario, conecta los componentes visuales a sus controladores y
     * carga las categorias disponibles para visualizacion y edicion.
     * <p>
     * Este metodo sigue los principios de configuracion inicial de fragmentos en Android,
     * permitiendo inicializar interacciones de usuario y comportamientos de la interfaz.
     *
     * @param view               La vista creada por el framework donde se initialize los
     *                           componentes y se establecen los listeners.
     * @param savedInstanceState Una instancia de {@link Bundle} que puede contener el estado
     *                           previamente guardado del fragmento o null si no existe estado
     *                           previo.
     * @throws NullPointerException Si un recurso esencial de la vista no es encontrado, como el
     *                              identificador especificado para un elemento visual.
     * @see #loadAllCategoriesAsListElements()
     * @see #setupAddCategoryButton()
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //? Navegacion de la aplicacion
        MaterialButton menuButton = view.findViewById(R.id.menuButtonForNavigation);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }

        //? Cargamos todos los componentes
        this.configureYourCategoriesViewAddCategoryMaterialButton =
                view.findViewById(R.id.configureYourCategoriesViewAddCategoryMaterialButton);
        this.configureYourCategoriesScrollViewForCategoriesLabel =
                view.findViewById(R.id.configureYourCategoriesScrollViewForCategoriesLabel);
        this.configureYourCategoriesLinearLayoutForCategoriesLabels =
                view.findViewById(R.id.configureYourCategoriesLinearLayoutForCategoriesLabels);

        //? Configuramos la UI, en especial el proceso del boton para anadir y la vista de las
        //? categorias
        loadAllCategoriesAsListElements();
        setupAddCategoryButton();
    }


    /**
     * Este metodo carga y presenta todas las categorias de gastos disponibles en la interfaz de
     * usuario dentro de un componente de tipo {@link LinearLayout}. Utiliza datos provenientes del
     * singleton {@link ApplicationDataPOJO}, cargando dinamicamente las vistas para cada
     * categoria.
     * <p>
     * Al trabajar con la lista de categorias, este metodo sigue los pasos:
     * <ul>
     *     <li>Limpia las vistas existentes del contenedor principal para asegurar que no haya duplicados.</li>
     *     <li>Obtiene la lista de categorias desde el singleton {@link ApplicationDataPOJO}.</li>
     *     <li>Por cada categoria en la lista, crea un contenedor vertical con sus datos asignados.</li>
     *     <li>Para cada categoria, genera vistas anidadas mostrando el nombre, el monto maximo mensual
     *     y el monto permitido por gasto.</li>
     *     <li>Incluye un icono interactivo para realizar acciones (como eliminar) asociadas a cada categoria.</li>
     *     <li>Asegura que se mantenga un diseno limpio agregando divisores y margenes entre las categorias.</li>
     * </ul>
     * <p>
     * Este metodo no tiene parametros de entrada ni retorna valores debido a que interactua directamente
     * con los componentes de la vista y el modelo de datos.
     *
     * @throws NullPointerException si ocurre un problema al acceder a las fuentes de recursos o si
     *                              el contexto requerido no esta disponible.
     */
    private void loadAllCategoriesAsListElements() {

        this.configureYourCategoriesLinearLayoutForCategoriesLabels.removeAllViewsInLayout();
        //?1. Cargamos todos los datos desde el Singleton de Datos
        List<ExpenseCategory> expenseCategoryList = ApplicationDataPOJO
                .getApplicationDataPOJOInstance().getApplicationDataPojoExpenseCategories();
        if (!expenseCategoryList.isEmpty()){
            expenseCategoryList.forEach(new Consumer<ExpenseCategory>() {
                @Override
                public void accept(ExpenseCategory expenseCategory) {
                    // 1.2 Creamos un layout vertical anterior para hacer un diseno lineal
                    LinearLayout verticalContainer = new LinearLayout(getContext());
                    LinearLayout.LayoutParams verticalParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    verticalContainer.setOrientation(LinearLayout.VERTICAL);
                    verticalContainer.setLayoutParams(verticalParams);

                    //? 2. Creamos un nuevo Linear Layout de Cada uno de estos para definir el grupo
                    LinearLayout containerForLabel = new LinearLayout(getContext());
                    LinearLayout.LayoutParams parametersForContainerview =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                          ViewGroup.LayoutParams.WRAP_CONTENT);
                    containerForLabel.setLayoutParams(parametersForContainerview);
                    containerForLabel.setGravity(Gravity.CENTER_VERTICAL);

                    TextView textViewForCategoryLabel = new TextView(getContext());
                    LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                    );
                    labelParams.gravity = Gravity.CENTER_VERTICAL;
                    textViewForCategoryLabel.setLayoutParams(labelParams);
                    textViewForCategoryLabel.setText(expenseCategory.getExpenseCategoryName());
                    textViewForCategoryLabel.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                 R.font.inter_bold));
                    textViewForCategoryLabel.setGravity(Gravity.END);
                    textViewForCategoryLabel.setTextSize(12);

                    //? 2.1 Creamos un textLabel para el maximo que se puede gastar
                    TextView textViewForCategoryMaxAmount = new TextView(getContext());
                    LinearLayout.LayoutParams maxAmountParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                    );
                    maxAmountParams.gravity = Gravity.CENTER_VERTICAL;
                    textViewForCategoryMaxAmount.setLayoutParams(maxAmountParams);
                    textViewForCategoryMaxAmount.setText(String.format("$%.2f",
                                                                            expenseCategory.getExpenseCategoryMaxPerMonthValue()));
                    textViewForCategoryMaxAmount.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                 R.font.inter_bold));
                    textViewForCategoryMaxAmount.setTextSize(12);
                    textViewForCategoryMaxAmount.setGravity(Gravity.END);

                    //? 2.2 Creamos un textlabel para el maximo por compra
                    TextView textViewForCategoryMaxPerPurchase = new TextView(getContext());
                    LinearLayout.LayoutParams maxPerPurchaseParams = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                    );
                    maxPerPurchaseParams.gravity = Gravity.CENTER_VERTICAL;
                    textViewForCategoryMaxPerPurchase.setLayoutParams(maxPerPurchaseParams);
                    textViewForCategoryMaxPerPurchase.setText(String.format("$%.2f",
                                                                            expenseCategory.getExpenseCategoryMaxPerExpenseValue()));
                    textViewForCategoryMaxPerPurchase.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                 R.font.inter_bold));
                    textViewForCategoryMaxPerPurchase.setGravity(Gravity.END);
                    textViewForCategoryMaxPerPurchase.setTextSize(12);
                    //? Creamos un mini icono para los botones
                    ImageView iconView = new ImageView(getContext());
                    LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                            dpToPx(24),
                            dpToPx(24)
                    );
                    iconParams.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
                    iconView.setLayoutParams(iconParams);
                    iconView.setImageResource(R.drawable.three_dots_icon);
                    iconView.setClickable(true);
                    iconView.setFocusable(true);
                    iconView.setImageTintList(ColorStateList.valueOf(
                            ResourcesCompat.getColor(getResources(),R.color.inputFieldColor,
                                                     null)));
                    //? Al boton tenemos que anadirle un listener para un onclick que nos dara un
                    // menu que nos permita eliminar la categoria
                    iconView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCategoryOptionsMenu(iconView,
                                                    expenseCategory);
                        }
                    });

                    View divider = new View(getContext());
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dpToPx(1)
                    );
                    dividerParams.topMargin = dpToPx(8);
                    divider.setLayoutParams(dividerParams);
                    divider.setBackgroundColor(Color.WHITE);

                    Space space = new Space(getContext());
                    LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dpToPx(10)
                    );
                    space.setLayoutParams(spaceParams);

                    containerForLabel.addView(iconView);
                    containerForLabel.addView(textViewForCategoryLabel);
                    containerForLabel.addView(textViewForCategoryMaxAmount);
                    containerForLabel.addView(textViewForCategoryMaxPerPurchase);

                    verticalContainer.addView(containerForLabel);
                    verticalContainer.addView(divider);
                    verticalContainer.addView(space);


                    configureYourCategoriesLinearLayoutForCategoriesLabels.addView(verticalContainer);
                }
            });
        }
    }

    /**
     * <body style='color: white'>
     * Convierte un valor dado en densidad de pixeles (dp) a su equivalente en pixeles reales en
     * base a la densidad de pantalla del dispositivo actual.
     * <p>
     * Este metodo utiliza la clase {@link TypedValue} para aplicar la dimension y convierte
     * automaticamente el valor de dp en pixeles usables, segun las especificaciones del
     * dispositivo. Es util para garantizar que las medidas y los margenes en la interfaz se vean
     * correctamente en diferentes tamanos y densidades de pantalla.
     * </p>
     *
     * @param dp El valor en dp (densidad independiente de pixeles) que se desea convertir.
     * @return El valor convertido en pixeles reales para la pantalla actual.
     * @throws NullPointerException Si {@code getResources()} o {@code getDisplayMetrics()} retorna
     *                              null, arrojara esta excepcion.
     *                              </body>
     */
    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
                                              );
    }


    //? Helper para mostar dialogos al usuario de confirmacion

    /**
     * <body style='color: white'>
     * Muestra un menu de opciones desplegable asociado a una categoria especifica de gastos,
     * utilizando un {@link ListPopupWindow} anclado a una vista determinada.
     * <p>
     * Este metodo sigue los siguientes pasos para interactuar con el usuario:
     * <ul>
     *     <li>Define un arreglo de opciones predefinidas ("Remove All Expenses", "Delete Category").</li>
     *     <li>Configura un {@link ListPopupWindow} para desplegar las opciones al usuario.</li>
     *     <li>Escucha la seleccion de opciones, ejecutando una accion distinta dependiendo
     *     de la opcion elegida:</li>
     *     <ul>
     *         <li><b>Remove All Expenses</b>: Muestra un dialogo de confirmacion antes de eliminar
     *         todos los gastos asociados a la categoria seleccionada.</li>
     *         <li><b>Delete Category</b>: Muestra un dialogo de confirmacion antes de eliminar
     *         la categoria seleccionada del sistema.</li>
     *     </ul>
     * </ul>
     * <p>
     * La lista desplegable es modal y se cierra automaticamente una vez que se selecciona una opcion.
     * Este metodo utiliza un {@link ArrayAdapter} para configurar la interfaz, y ancla la vista
     * a traves del parametro {@link ImageView}.
     * </p>
     *
     * @param anchorView La vista que sera utilizada como referencia para anclar el menu
     *                   desplegable. Debe ser una instancia valida de {@link ImageView}.
     * @param category   La categoria de tipo {@link ExpenseCategory} que se asociara a las acciones
     *                   disponibles en el menu.
     * @throws NullPointerException Si {@code anchorView} o {@code category} son nulos, o si los
     *                              recursos requeridos no estan disponibles.
     *                              </body>
     */
    private void showCategoryOptionsMenu(ImageView anchorView, ExpenseCategory category) {
        //? 1. Creamos una lista de Strings, ya que nuestro querido ArrayAdapter requiere un 
        // arreglo y no una lista para mostrar los componentes en la view.
        String[] options = new String[]{"Remove All Expenses", "Delete Category"};

        
        //? 2. Creamos un array adapter, el cual transforma los objetos dentro de nuestro arreglo
        // hacia componenters visuales. Los componentes que nos genera se definen visualmente con
        // un resource layout que le pasamos, en este caso un background oscuro, letras verdes y 
        // borde para que se distingan las opciones.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.drop_down_menu_for_user_categories,
                options
        );

        //? 3. Creamos un ListPopUpView, un componente visual que simula el comportamiento de una
        // combobox en JavaFX pero sin tener el boton grande y el componente en general, este se 
        // genera solamente cuando nosotros hacemos click en uno de los iconos al lado de la 
        // categora. esto nos facilita tener el conocimiento de que categoria estamos 
        // seleccionando para poder eliminarla o eliminar sus consumos
        ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setWidth(500);
        listPopupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        com.google.android.material.R.drawable.m3_tabs_transparent_background
                , null));
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setModal(true);
        //? 3.1 Le  enchufamos un listener para cuando hacemos click en un elemento a nuestra 
        // combobox, la idea de este metodo es iterar sobre las opciones posibles, que en si son 
        // dos, y dependiendo de la posicion (position determina si es la opcion 0 de remover o 1
        // de eliminar) y mostramos dialogos de confirmacion. Sorprendentemente Android tiene 
        // componentes que se parecen a los alerts o dialogs de JavaFX, esto no fue muy diferente
        listPopupWindow.setOnItemClickListener((parent, 
                                                view, 
                                                position, 
                                                id) -> {
            listPopupWindow.dismiss();

            if (position == 0) {
                showConfirmationDialog(
                        "Remove All Expenses",
                        "Are you sure you want to remove all expenses from this category?",
                        () -> {
                            ApplicationDataPOJO.getApplicationDataPOJOInstance()
                                    .dropAllExpensesPerCategory(category);
                            AddExpenseView.createAndShowToast(getContext(),
                                                              "All expenses " +
                                                                      "removed", R.layout.custom_toast_for_success);
                            loadAllCategoriesAsListElements();
                        });

            } else {
                showConfirmationDialog(
                        "Delete Category",
                        "Are you sure you want to delete this category?",
                        () -> {
                            ApplicationDataPOJO.getApplicationDataPOJOInstance()
                                    .removeCategoryFromCategoryList(category);
                            AddExpenseView.createAndShowToast(getContext(),
                                                              "Category removed",
                                                              R.layout.custom_toast_for_success);
                            loadAllCategoriesAsListElements();
                        });
            }

        });

        listPopupWindow.show();
    }

    /**
     * <body style='color: white'>
     * Muestra un dialogo de confirmacion al usuario utilizando el componente
     * {@link MaterialAlertDialogBuilder}. Este dialogo permite al usuario confirmar o cancelar una
     * accion especifica, con la posibilidad de ejecutar una operacion definida por un
     * {@link Runnable} en caso de confirmacion.
     * <p>
     * Este metodo presenta la siguiente funcionalidad:
     * </p>
     * <ul>
     *     <li>Crea un dialogo interactivo con un titulo, un mensaje descriptivo y dos botones.</li>
     *     <li>El boton "Proceed" ejecuta el {@link Runnable} proporcionado si se selecciona.</li>
     *     <li>El boton "Cancel Procedure" simplemente cierra el dialogo sin realizar ninguna
     *     accion adicional.</li>
     *     <li>Utiliza recursos de diseño personalizados para estilizar los componentes del dialogo
     *     (colores, esquinas redondeadas y tipografía).</li>
     * </ul>
     * <p>
     * Adicionalmente, el metodo configura el estilo visual del dialogo durante la rutina
     * {@code setOnShowListener()}, asegurando que los textos y botones se adapten al diseño general
     * de la aplicacion actual.
     * </p>
     *
     * @param title     El titulo del dialogo, mostrado como encabezado. Este valor debe ser una
     *                  cadena no nula ni vacia.
     * @param message   El mensaje informativo mostrado en el cuerpo del dialogo. Este valor debe
     *                  ser una cadena no nula ni vacia.
     * @param onConfirm Una instancia de {@link Runnable} que define la accion a ejecutar cuando el
     *                  usuario elige confirmar la operacion. Debe ser distinto de null.
     * @throws NullPointerException  Si {@code title}, {@code message}, {@code onConfirm} o los
     *                               recursos requeridos para el dialogo son null.
     * @throws IllegalStateException Si el metodo es llamado en un {@link Context} que no posee un
     *                               ciclo de vida valido o contexto adecuado.
     *                               </body>
     */
    public void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        //? 1. Creamos el builder base de nuestro dialogo, lo que hacemos es simplemente entrar y
        // pasar los componentes de nuestra vista principal rapidamente y cargamos lo estructural
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Proceed", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Cancel Procedure", null)
                .setBackground(getResources().getDrawable(R.drawable.rounded_corners_for_text_input, null));

        //? 2. En este punto tranformarmos el builder a un AlertDialog real, y le enchufamos la 
        // el listener adecuado para cuando mostremos la UI. Hacemos esto ya que cuando se 
        // muestre, al inicio se mostraba con el look de material UI y necesitabamos un look 
        // parecido al de la App 
        AlertDialog dialogForAlert = builder.create();
        dialogForAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //? 2.1 Ponemos el color del texto tanto de titulo, cuerpo y botones
                TextView title = dialogForAlert.findViewById(android.R.id.title);
                if (title != null) {
                    title.setTextColor(ResourcesCompat.getColor(getResources(), R.color.textColor
                            , null));
                }
                TextView body = dialogForAlert.findViewById(android.R.id.message);
                if (body != null) {
                    body.setTextColor(ResourcesCompat.getColor(getResources(), R.color.textColor,
                                                               null));
                }
                Button positiveButton = dialogForAlert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = dialogForAlert.getButton(DialogInterface.BUTTON_NEGATIVE);

                positiveButton.setAllCaps(false);
                negativeButton.setAllCaps(false);
                positiveButton.setBackgroundColor(getResources().getColor(R.color.textColor, null));
                negativeButton.setBackgroundColor(getResources().getColor(R.color.backgroundTwo, null));
                positiveButton.setTextColor(getResources().getColor(R.color.inputFieldColor, null));
                negativeButton.setTextColor(getResources().getColor(R.color.inputFieldColor, null));
            }
        });
        //? 3. Mostramos al final la alerta y continuamos normalmente.
        dialogForAlert.show();
    }

    /**
     * <body style='color: white'>
     * Configura el boton para anadir nuevas categorias en la aplicacion. Este metodo asociara un
     * {@link View.OnClickListener} al boton relevante, el cual mostrara un dialogo de pantalla
     * completa al accionarse, permitiendo al usuario ingresar y guardar los datos necesarios para
     * una nueva categoria.
     * <p>
     * El metodo sigue los siguientes pasos:
     * </p>
     * <ul>
     *     <li>Crea un dialogo de pantalla completa utilizando una vista predefinida en el diseño XML.</li>
     *     <li>Obtiene y configura los elementos principales del dialogo, incluyendo campos de texto, botones, y layouts.</li>
     *     <li>Agrega validaciones para garantizar que los campos obligatorios no esten vacios, y que
     *     los valores numericos cumplan condiciones especificas (por ejemplo, mayores que cero y consistentes entre si).</li>
     *     <li>Guarda la nueva categoria en el modelo a traves de {@link ApplicationDataPOJO},
     *     recarga la lista de categorias visibles y cierra el dialogo al completarse exitosamente.</li>
     *     <li>Incluye elementos interactivos como botones de cancelar y guardar, con mensajes de error
     *     personalizados para el usuario en caso de entradas invalidas.</li>
     * </ul>
     *
     * @throws NullPointerException Si {@link requireContext()}, {@link findViewById(int)} o
     *                              recursos asociados al diseño no estan disponibles.
     *                              </body>
     */
    private void setupAddCategoryButton() {
        this.configureYourCategoriesViewAddCategoryMaterialButton.setOnClickListener(v -> {
            //?1.  Creamos un dialogo que vamos a usar para mostrar la imagen principal de la
            // creacion de categorias, esta vista es un dialogo aislado de pantalla completa.
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE /*Utilizamos para colocar la
            view sin un titulo forzado*/);
            dialog.setContentView(R.layout.define_new_category_dialog);

            //? 2. Obtenemos la ventana en la que se encuentra nuestro dialogo, con esto podemos
            // ponerle el tamano completo y con el color que tiene que seguir, igual como
            // cargamos la vista no deberia de haber problemas.
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                 ViewGroup.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawable(new ColorDrawable(
                        ResourcesCompat.getColor(getResources(), R.color.backgroundOne, null)));
            }

            //? 3. Jalamos todos los componentes para su modificacion.
            MaterialAutoCompleteTextView categoryNameDefinitionTextInputEditText =
                    dialog.findViewById(R.id.categoryNameDefinitionTextInputEditText);
            MaterialAutoCompleteTextView categoryMonthlyTotalDefinitionTextInputEditText =
                    dialog.findViewById(R.id.categoryMonthlyTotalDefinitionTextInputEditText);
            MaterialAutoCompleteTextView categoryMaximumExpenseNumberDecimalValueTextInputEditText =
                    dialog.findViewById(R.id.categoryMaximumExpenseNumberDecimalValueTextInputEditText);
            MaterialButton cancelCategoryRegistrationMaterialButton =
                    dialog.findViewById(R.id.cancelCategoryRegistrationMaterialButton);
            MaterialButton saveNewCategoryMaterialButton =
                    dialog.findViewById(R.id.saveNewCategoryMaterialButton);
            TextInputLayout categoryCreationCategoryNameTextInputLayout =
                    dialog.findViewById(R.id.categoryCreationCategoryNameTextInputLayout);
            TextInputLayout categoryMaxMonthlyTotalDefinitionTextInputLayout =
                    dialog.findViewById(R.id.categoryMaxMonthlyTotalDefinitionTextInputLayout);
            TextInputLayout categoryCreationMaxExpensePerExpenseTextInputLayout =
                    dialog.findViewById(R.id.categoryCreationMaxExpensePerExpenseTextInputLayout);


            //? 3.1 Conectamos los componentes de los textos para tener guardias, la idea sera
            // validar que el texto se limpie cuando se empiece a escribir de nuevo
            categoryNameDefinitionTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        categoryCreationCategoryNameTextInputLayout.setHint("");
                    }
                }
            });
            categoryMonthlyTotalDefinitionTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        categoryMaxMonthlyTotalDefinitionTextInputLayout.setHint("");
                    }
                }
            });
            categoryMaximumExpenseNumberDecimalValueTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        categoryCreationMaxExpensePerExpenseTextInputLayout.setHint("");
                    }
                }
            });

            //? 3.2 Definimos los listeners para los botones, validando que existan las entradas
            cancelCategoryRegistrationMaterialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //? 3.2.1 Si cancelamos, tenemos que limpiar todos los campos
                    categoryNameDefinitionTextInputEditText.setText("");
                    categoryMonthlyTotalDefinitionTextInputEditText.setText("");
                    categoryMaximumExpenseNumberDecimalValueTextInputEditText.setText("");
                    //? 3.2.2 Si cancelamos, tenemos que cerrar la actividad
                    dialog.dismiss();
                }
            });
            saveNewCategoryMaterialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //? 3.2.1 Si guardamos, tenemos que validar que existan las entradas
                    //? 3.2.1.1 Validamos que la entrada del nombre de la categoria no sea nula
                    boolean isCategoryNameEmpty =
                            categoryNameDefinitionTextInputEditText
                                    .getText().toString().trim().isEmpty();
                    //? 3.2.1.2 Validamos que la entrada del monto mensual no sea nula
                    boolean isCategoryMonthlyTotalEmpty =
                            categoryMonthlyTotalDefinitionTextInputEditText
                                    .getText().toString().trim().isEmpty();
                    //? 3.2.1.3 Validamos que la entrada del monto maximo por gasto no sea nula
                    boolean isCategoryMaximumExpenseEmpty =
                            categoryMaximumExpenseNumberDecimalValueTextInputEditText
                                    .getText().toString().trim().isEmpty();
                    if (!isCategoryNameEmpty){
                        //? 3.2.2 Tomamos el valor y lo guardamos
                        String categoryName =
                                categoryNameDefinitionTextInputEditText.getText().toString();
                        if (!isCategoryMonthlyTotalEmpty){
                            //? 3.2.3 Tomamos el valor y lo guardamos
                            String categoryMonthlyTotal =
                                    categoryMonthlyTotalDefinitionTextInputEditText.getText().toString();
                            BigDecimal categoryMonthlyTotalBigDecimal =
                                    new BigDecimal(categoryMonthlyTotal);
                            if (!isCategoryMaximumExpenseEmpty){
                                //? 3.2.4 Tomamos el valor y lo guardamos
                                String categoryMaximumExpense =
                                        categoryMaximumExpenseNumberDecimalValueTextInputEditText
                                                .getText().toString();
                                BigDecimal categoryMaximumExpenseBigDecimal =
                                        new BigDecimal(categoryMaximumExpense);
                                //? 3.2.5 Revisamos si los valores leidos son mayores que cero, y
                                // si no son el mensual menor que el permitido
                                if (categoryMonthlyTotalBigDecimal.compareTo(BigDecimal.ZERO) > 0
                                        && categoryMaximumExpenseBigDecimal.compareTo(BigDecimal.ZERO) > 0
                                        && categoryMaximumExpenseBigDecimal.compareTo(categoryMonthlyTotalBigDecimal) < 0){
                                    //? 3.2.6 Si  esta bien, creamos la categoria y la guardamos
                                    ExpenseCategory category =
                                            new ExpenseCategory(categoryName,
                                                                categoryMaximumExpenseBigDecimal,
                                                                categoryMonthlyTotalBigDecimal);
                                    ApplicationDataPOJO.getApplicationDataPOJOInstance()
                                            .addCategoryIntoCategoryList(category);
                                    //? 3.2.7 Limpiamos los campos
                                    categoryNameDefinitionTextInputEditText.setText("");
                                    categoryMonthlyTotalDefinitionTextInputEditText.setText("");
                                    categoryMaximumExpenseNumberDecimalValueTextInputEditText.setText("");
                                    //? 3.2.8 Cerramos el dialogo
                                    loadAllCategoriesAsListElements();
                                    AddExpenseView.createAndShowToast(getContext(),
                                                                       "Category created " +
                                                                               "Successfully",
                                                                       R.layout.custom_toast_for_success);
                                    dialog.dismiss();
                                } else {
                                    //? 3.2.9 Si no, mostramos un mensaje de error
                                    //? 3.2.9.1 Si el valor del mensual es menor que cero
                                    // mostramos el tip
                                    if (categoryMonthlyTotalBigDecimal.compareTo(BigDecimal.ZERO) < 0){
                                        AddExpenseView.createAndShowToast(getContext(),
                                                       "Monthly total must be greater than zero",
                                                       R.layout.custom_toast_for_errors);
                                    }
                                    //? 3.2.9.2 Si el valor del maximo por gasto es menor que cero
                                    // mostramos el tip
                                    if (categoryMaximumExpenseBigDecimal.compareTo(BigDecimal.ZERO) < 0){
                                        AddExpenseView.createAndShowToast(getContext(),
                                                       "Maximum expense must be greater than zero",
                                                       R.layout.custom_toast_for_errors);
                                    }
                                    //? 3.2.9.3. Si los valores no cuadran con el sentido de
                                    // tener un budget mayor al total de compra
                                    if (categoryMaximumExpenseBigDecimal.compareTo(categoryMonthlyTotalBigDecimal) > 0){
                                        AddExpenseView.createAndShowToast(getContext(),
                                                       "Maximum expense must not be greater than the " +
                                                               "monthly " +
                                                               "total per month",
                                                       R.layout.custom_toast_for_errors);
                                    }
                                    //? 3.2.9.4 SI los valores son iguales mostramos un mensaje
                                    // de error igual
                                    if (categoryMaximumExpenseBigDecimal.compareTo(categoryMonthlyTotalBigDecimal) == 0){
                                        AddExpenseView.createAndShowToast(getContext(),
                                                       "Maximum expense must not be equal to the " +
                                                               "monthly " +
                                                               "total",
                                                       R.layout.custom_toast_for_errors);
                                    }
                                }
                            }
                            else {
                                //? 3.2.4.1 Si el vlaor del maximo gasto mensual por compra no
                                // esta registrado
                                AddExpenseView.createAndShowToast(getContext(),
                                                                  "Maximum expense must be set",
                                                                  R.layout.custom_toast_for_errors);
                            }
                        } else {
                            //? 3.2.3.1 Si el valor del mensual no esta registrado
                            AddExpenseView.createAndShowToast(getContext(),
                                                              "Monthly total must be set",
                                                              R.layout.custom_toast_for_errors);
                        }
                    } else {
                        //? 3.2.2.1 Si el valor del nombre de la categoria no esta registrado
                        AddExpenseView.createAndShowToast(getContext(),
                                                          "Category name must be set",
                                                          R.layout.custom_toast_for_errors);
                    }
                }
            });

            //? 4. Salimos y mostramos el dialogo
            dialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //? Navegacion de la aplicacion
        MaterialButton menuButton = getView().findViewById(R.id.menuButtonForNavigation);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }
        loadAllCategoriesAsListElements();
        setupAddCategoryButton();

    }
}
