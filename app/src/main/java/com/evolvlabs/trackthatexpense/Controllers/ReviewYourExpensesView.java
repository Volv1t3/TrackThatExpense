package com.evolvlabs.trackthatexpense.Controllers;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ApplicationDataPOJO;
import com.evolvlabs.trackthatexpense.PersistencyEngine.Expense;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ExpenseCategory;
import com.evolvlabs.trackthatexpense.R;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


/**
 * @author : Santiago Arellano
 * @date : 21st-mar-2025
 * @description: El presente archivo incluye la implementacion del controlador para el 
 * review_your_expenses_view.xml, que incluye funcionalidad para listar gastos, eliminar un solo 
 * gasto y visualizar gastos por categoria.
 */
public class ReviewYourExpensesView extends Fragment {

    private LinearLayout reviewYourExpensesExpensesLinearLayout;
    private AutoCompleteTextView expenseCategoryInReviewYourExpensesViewAutoCompleteTextView;
    private TextInputLayout textInputLayoutForCategory;

    /**
     * Este metodo se encarga de inflar el archivo de diseño correspondiente al fragmento
     * «ReviewYourExpensesView», preparandolo para ser desplegado en la interfaz de usuario.
     *
     * <p>El archivo XML «review_your_expenses_view» se utiliza para definir la representacion
     * grafica del fragmento. Este metodo se llama automaticamente durante el ciclo de vida del
     * fragmento.</p>
     *
     * @param inflater           Un objeto {@link LayoutInflater} que permite inflar vistas desde un
     *                           archivo XML.
     * @param container          El contenedor padre donde se agregara la vista inflada, puede ser
     *                           nulo si no hay necesidad de anidarla.
     * @param savedInstanceState Una instancia {@link Bundle} que contiene el estado previamente
     *                           guardado del fragmento, si se encuentra disponible.
     * @return Devuelve un objeto {@link View} que representa la interfaz de usuario inflada del
     * fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.review_your_expenses_view, container, false);
    }

    /**
     * <body style="color:white;">
     * Metodo llamado despues de que se ha creado la vista base del fragmento. Este metodo
     * inicializa las referencias a los componentes de la interfaz de usuario para vincularlos con
     * la logica de negocio y gestionar su comportamiento.
     *
     * <p>Realiza las siguientes operaciones principales:</p>
     * <ul>
     *   <li>Inicializa los componentes visuales desde el archivo XML con sus IDs.</li>
     *   <li>Configura el menu de navegacion para mostrar el drawer desde un boton.</li>
     *   <li>Establece conexiones para manejar las interacciones de interfaz mediante listeners
     *   y metodos auxiliares.</li>
     * </ul>
     *
     * @param view               La vista raiz inflada del fragmento, donde se encuentran los
     *                           componentes definidos en XML.
     * @param savedInstanceState Contiene el estado previamente guardado del fragmento en caso de
     *                           restauraciones de procesos, puede ser nulo.
     * @throws NullPointerException Si alguno de los IDs requeridos en el archivo XML no se
     *                              inicializa correctamente, lo que implica que no existe en el
     *                              archivo del diseño.
     *                              </body>
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //? 1. Inicializamos las intancias de los componentes para poder manejarlos
        // programaticamente
        this.reviewYourExpensesExpensesLinearLayout =
                (LinearLayout)
                        view.findViewById(R.id.reviewYourExpensesExpensesLinearLayout);
        this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView =
                (AutoCompleteTextView)
                        view.findViewById(R.id.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView);
        this.textInputLayoutForCategory =
                (TextInputLayout)
                        view.findViewById(R.id.textInputLayoutForCategory);
        clearFieldsBeforeLeaving();
        //? 2. Inicializamos la navegacion, es el componente primordial ya que facilita la vida
        // en la aplicacion al usuario
        MaterialButton menuButton = view.findViewById(R.id.menuButtonForNavigation);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }

        //? 3. Inicializamos el resto de la UI
        initializeDropDownMenuConnectionOnAutoComplete();
    }

    /**
     * <body style="color:white;">
     * Este metodo configura la funcionalidad de un menu desplegable conectado a un campo de entrada
     * de texto AutoCompleteTextView, poblado con las categorias de gasto disponibles.
     * <p>
     * Realiza las siguientes operaciones principales:
     * <ul>
     *   <li>Obtiene la lista de categorias de gasto desde la instancia Singleton {@link ApplicationDataPOJO},
     *   verificando si la lista existe o si esta vacia.</li>
     *   <li>Crea un adaptador para visualizar las categorias en un menu desplegable.</li>
     *   <li>Configura el menu desplegable para mostrar las opciones cuando el usuario hace clic
     *   en el campo de texto correspondiente.</li>
     *   <li>Maneja la seleccion del usuario actualizando la interfaz con la categoria elegida,
     *   utilizando {@link initializeLinearLayoutBasedOnSelection} para actualizar visualmente los gastos de
     *   la categoria seleccionada.</li>
     * </ul>
     *
     * <p>El metodo utiliza conceptos fundamentales como patrones Singleton para la gestion de datos
     * globales y adaptadores de Android para gestionar elementos visuales de forma dinamica.</p>
     *
     * @throws NullPointerException  Si ocurre un error en los componentes visuales (e.g.,
     *                               AutoCompleteTextView) al no ser inicializados correctamente o
     *                               si {@link ListPopupWindow} no encuentra un recurso valido.
     * @throws IllegalStateException Si el contexto actual no es valido o no esta asociado a una
     *                               actividad.
     *                               </body>
     */
    private void initializeDropDownMenuConnectionOnAutoComplete() {
        /*
         * La idea de esta seccion, es definir un metodo que nos permita agarrar el contenido
         * de los campos del Singleton de las expenses, y usar los nombres de los campos como
         * objetos de la lista (visuales) e internamente registrar en un estado en base a ese
         * objeto el actual Category object con lo que podemos anadir y manejar el gasto!*/

        this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView.setOnClickListener(v -> {
            //? 1. Jalamos la informacion del Singleton
            List<ExpenseCategory> categories = ApplicationDataPOJO
                    .getApplicationDataPOJOInstance()
                    .getApplicationDataPojoExpenseCategories();

            if (categories.isEmpty()) {
                AddExpenseView.createAndShowToast(getContext(),
                                                  "No expense categories found",
                                                  R.layout.custom_toast_for_errors);
                return;
            }

            //? 2. Creamos un adapter para el arreglo de categorias (internamente el objeto que 
            // pasamos se convierte en un arreglo de objetos, cuya representacion se da a traves 
            // del toString, que en el caso de categorias solo incluye el nombre
            ArrayAdapter<ExpenseCategory> adapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.drop_down_menu_for_user_categories,
                    categories
            );

            //? 3. Creamos otro semi-combobox para la aplicacion, configurando la informacion en 
            // base al formato definido para las categorias, solo que en este caso cambiando los 
            // datos.
            ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());
            listPopupWindow.setAdapter(adapter);
            listPopupWindow.setAnchorView(this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView);
            listPopupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                            com.google.android.material.R.drawable.m3_tabs_transparent_background
                    , null));
            listPopupWindow.setModal(true);

            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listPopupWindow.dismiss();
                    //? Cargamos programaticamente todos los componentes haciendo una llamada al
                    // metodo que tiene que hacerlo
                    textInputLayoutForCategory.setHint("");
                    initializeLinearLayoutBasedOnSelection(adapter.getItem(position));
                    expenseCategoryInReviewYourExpensesViewAutoCompleteTextView
                            .setText(adapter.getItem(position).getExpenseCategoryName());
                }
            });

            listPopupWindow.show();
        });

    }

    /**
     * <body style="color:white;">
     * Este metodo maneja la visualizacion programatica de los gastos asociados a una categoria
     * especifica seleccionada por el usuario en el menu desplegable de categorias.
     *
     * <p>Realiza las siguientes operaciones:</p>
     * <ul>
     *   <li>Recupera todos los gastos asociados a la categoria proporcionada mediante el singleton
     *   {@link ApplicationDataPOJO}.</li>
     *   <li>Ordena la lista de gastos en orden descendente basado en el valor registrado
     *   de cada gasto.</li>
     *   <li>Crea elementos visuales dinamicamente para cada gasto en la lista, formateandolos
     *   en un diseño de layouts anidados dentro de {@link LinearLayout}.</li>
     *   <li>Incluye controles interactivos, como iconos para eliminar un gasto, con la
     *   visualizacion de un modal de confirmacion antes de realizar esta operacion.</li>
     *   <li>Configura la interfaz utilizando colores, fuentes y formatos definidos
     *   en los recursos de la aplicacion.</li>
     * </ul>
     *
     * <p>El metodo combina el uso de componentes visuales de Android, manipulaciones de
     * listas y Java Streams para procesar los datos y presentarlos dinamicamente.</p>
     *
     * @param expenseCategory El objeto {@link ExpenseCategory} seleccionado por el usuario,
     *                        necesario para recuperar y gestionar los gastos asociados.
     * @throws IllegalStateException Si el contexto relacionado al fragmento no esta correctamente
     *                               inicializado o es invalido.
     * @throws NullPointerException  Si alguno de los elementos visuales no puede ser inicializado o
     *                               las referencias a las vistas son nulas.
     *                               </body>
     */
    private void initializeLinearLayoutBasedOnSelection(ExpenseCategory expenseCategory) {
        this.reviewYourExpensesExpensesLinearLayout.removeAllViews();
        //? 1. Tomamos todos los expenses de esa categoria y los cargamos en memoria
        List<Expense> expenseList =
                ApplicationDataPOJO.getApplicationDataPOJOInstance()
                        .getAllExpensesPerCategory(expenseCategory);
        expenseList.sort(Comparator
                                 .comparing(Expense::getExpenseValueRegistered).reversed());
        expenseList.forEach(new Consumer<Expense>() {
            @Override
            public void accept(Expense expense) {
                //? 2. Definimos Un vertical layout con tres componentes: horizontal layout con
                // datos de consumo, separator y space
                LinearLayout verticalLayoutParent = new LinearLayout(getContext());
                LinearLayout.LayoutParams verticalLayoutParentLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                verticalLayoutParent.setOrientation(LinearLayout.VERTICAL);
                verticalLayoutParent.setLayoutParams(verticalLayoutParentLayoutParams);

                //? 2.1 Definimos un horizontal layout para poner los componentes principales de
                // nuestra vista por expense
                LinearLayout horizontalLayoutChildrenOne = new LinearLayout(getContext());
                LinearLayout.LayoutParams horizontalLayoutChildrenOneLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                horizontalLayoutChildrenOne.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayoutChildrenOne.setLayoutParams(horizontalLayoutChildrenOneLayoutParams);

                //? 2.1.1 Creamos un icono al cual le vamos a conectar la opcion de borrar el
                // expense con un modal
                ImageView removeExpenseIconViewHorizontalChildrenOne = new ImageView(getContext());
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                        dpToPx(24),
                        dpToPx(24)
                );
                iconParams.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
                removeExpenseIconViewHorizontalChildrenOne.setLayoutParams(iconParams);
                removeExpenseIconViewHorizontalChildrenOne.setImageResource(R.drawable.subtract_icon);
                removeExpenseIconViewHorizontalChildrenOne.setImageTintList(
                        ColorStateList.valueOf(
                                ResourcesCompat.getColor(getResources(),
                                                         R.color.inputFieldColor, null)));
                removeExpenseIconViewHorizontalChildrenOne.setClickable(true);
                removeExpenseIconViewHorizontalChildrenOne.setFocusable(true);
                removeExpenseIconViewHorizontalChildrenOne.setPadding(0,0,30,0);

                //? 2.1.2 Creamos una label para la categoria de nuestro consumo
                TextView expenseCategoryLabelHorizontalChildrenOne = new TextView(getContext());
                LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                labelParams.weight =1;
                labelParams.gravity = Gravity.CENTER_VERTICAL;
                labelParams.setMarginEnd( dpToPx(8));
                expenseCategoryLabelHorizontalChildrenOne.setLayoutParams(labelParams);
                expenseCategoryLabelHorizontalChildrenOne.setText(expense.getExpenseCategory());
                expenseCategoryLabelHorizontalChildrenOne.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                              R.font.inter_bold));
                expenseCategoryLabelHorizontalChildrenOne.setTextColor(ResourcesCompat.getColor(getResources(),
                                                                                             R.color.textColor,
                                                                                             null));
                expenseCategoryLabelHorizontalChildrenOne.setGravity(Gravity.START);
                expenseCategoryLabelHorizontalChildrenOne.setTextSize(12);

                //? 2.1.3 Creamos una label para la fecha del consumo
                TextView expenseDateLabelHorizontalChildrenOne = new TextView(getContext());
                LinearLayout.LayoutParams labelParams2 = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                labelParams2.gravity = Gravity.CENTER_VERTICAL;
                labelParams2.weight =1;
                labelParams2.setMarginEnd( dpToPx(8));
                expenseDateLabelHorizontalChildrenOne.setLayoutParams(labelParams2);
                expenseDateLabelHorizontalChildrenOne.setText(String.format("%s",
                                new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                                    .format(expense.getExpenseDateRegistered())));
                expenseDateLabelHorizontalChildrenOne.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                              R.font.inter_bold));
                expenseDateLabelHorizontalChildrenOne.setTextColor(ResourcesCompat.getColor(getResources(),
                                                                                             R.color.textColor,
                                                                                             null));
                expenseDateLabelHorizontalChildrenOne.setTextSize(12);
                //? 2.1.4 Creamos una label para el valor del consumo
                TextView expenseValueLabelHorizontalChildrenOne = new TextView(getContext());
                LinearLayout.LayoutParams labelParams3 = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                labelParams3.weight =1;
                labelParams3.setMarginEnd( dpToPx(8));
                labelParams3.gravity = Gravity.CENTER_VERTICAL;
                expenseValueLabelHorizontalChildrenOne.setLayoutParams(labelParams3);
                expenseValueLabelHorizontalChildrenOne.setText(String.format("$%.2f",
                                expense.getExpenseValueRegistered()));
                expenseValueLabelHorizontalChildrenOne.setTypeface(ResourcesCompat.getFont(getContext(),
                                                                                              R.font.inter_bold));
                expenseValueLabelHorizontalChildrenOne.setTextColor(ResourcesCompat.getColor(getResources(),
                                                                                             R.color.textColor,
                                                                                             null));
                expenseValueLabelHorizontalChildrenOne.setTextSize(12);

                //? 2.1.5 Anadimos un listener de click al icono, con esto nos encargamos de
                // enchufarle un modal de dialogo para eliminar el contenido
                removeExpenseIconViewHorizontalChildrenOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmationDialog(
                                "Remove Expense",
                                "Are you sure you want to remove this expense?",
                                () -> {
                                    ApplicationDataPOJO.getApplicationDataPOJOInstance()
                                            .removeExpenseFromExpenseMap(expense);
                                    AddExpenseView.createAndShowToast(getContext(),
                                                                      "Expense successfully removed",
                                                                      R.layout.custom_toast_for_success);
                                    initializeLinearLayoutBasedOnSelection(expenseCategory);
                                }
                        );
                    }
                });

                //? 2.1.6 Conectamos los componentes al horizontal layout
                horizontalLayoutChildrenOne.addView(removeExpenseIconViewHorizontalChildrenOne);
                horizontalLayoutChildrenOne.addView(expenseCategoryLabelHorizontalChildrenOne);
                horizontalLayoutChildrenOne.addView(expenseDateLabelHorizontalChildrenOne);
                horizontalLayoutChildrenOne.addView(expenseValueLabelHorizontalChildrenOne);

                //?2.1.7 Conectamos el horizontal layout al vertical layout
                verticalLayoutParent.addView(horizontalLayoutChildrenOne);

                //? 2.2 Creamos un separador lineal blanco
                View divider = new View(getContext());
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dpToPx(1)
                );
                dividerParams.topMargin = dpToPx(8);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(Color.WHITE);
                //? 2.2.1 Anadimos el divider al layout vertical
                verticalLayoutParent.addView(divider);

                //? 2.3 Creamos un Space de 15dp
                Space space = new Space(getContext());
                LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dpToPx(15)
                );
                space.setLayoutParams(spaceParams);
                //? 2.3.1 Anadimos el space al layout vertical
                verticalLayoutParent.addView(space);
                //? 2.4 Anadimos el vertical layout al layout padre
                reviewYourExpensesExpensesLinearLayout.addView(verticalLayoutParent);
            }
        });

    }

    /**
     * <body style="color:white;">
     * Este metodo convierte una medida dada en dp (density-independent pixels) a su equivalente en
     * px (pixels) basandose en la densidad de pantalla del dispositivo.
     *
     * <p>Utiliza la clase {@link TypedValue} y el contexto actual para calcular la conversion
     * con la densidad escala del dispositivo obtenida de {@link android.util.DisplayMetrics}.</p>
     *
     * @param dp La medida en dp que se desea convertir a px.
     * @return La medida en px que equivale al valor dp proporcionado.
     * @throws IllegalStateException Si no se puede acceder a {@link android.content.res.Resources}
     *                               para obtener la densidad de pantalla.
     *                               </body>
     */
    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
                                              );
    }

    /**
     * <body style="color:white;">
     * Este metodo muestra un cuadro de dialogo de confirmacion con un mensaje y dos botones: uno
     * para proceder con una accion y otro para cancelar.
     *
     * <p>El metodo utiliza {@link MaterialAlertDialogBuilder} para construir un dialogo
     * personalizado,
     * que incluye configuracion de estilo para los componentes visuales como color del texto,
     * botones y borde del dialogo.</p>
     *
     * <p>Realiza las siguientes operaciones principales:</p>
     * <ul>
     *   <li>Muestra un titulo y mensaje definidos en los parametros.</li>
     *   <li>Asocia el boton "Proceed" a ejecutar una operacion Runnable proporcionada como parametro.</li>
     *   <li>Permite cancelar el dialogo mediante el boton "Cancel Procedure".</li>
     *   <li>Personaliza los colores y estilos visuales del cuadro de dialogo y sus botones.</li>
     * </ul>
     *
     * <p>Este metodo utiliza componentes avanzados de Android como {@link AlertDialog} y la clase
     * {@link DialogInterface} para manejar acciones del usuario al interactuar con el dialogo.</p>
     *
     * @param title     El titulo que se muestra en el cuadro de dialogo.
     * @param message   El mensaje informativo o de confirmacion que se muestra al usuario.
     * @param onConfirm Una tarea {@link Runnable} que se ejecuta al presionar el boton "Proceed".
     * @throws IllegalStateException Si el contexto actual es nulo o no esta vinculado a una
     *                               actividad valida.
     * @throws NullPointerException  Si algun componente del dialogo no puede inicializarse
     *                               correctamente.
     *                               </body>
     */
    public void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Proceed", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Cancel Procedure", null)
                .setBackground(getResources().getDrawable(R.drawable.rounded_corners_for_text_input, null));

        AlertDialog dialogForAlert = builder.create();
        dialogForAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //? Ponemos el color del texto tanto de titulo, cuerpo y botones
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
        dialogForAlert.show();
    }

    /**
     * Called when the fragment is no longer in use.  This is called after {@link #onStop()} and
     * before {@link #onDetach()}.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearFieldsBeforeLeaving();
    }
    @Override
    public void onResume() {
        super.onResume();
        clearFieldsBeforeLeaving();
        initializeDropDownMenuConnectionOnAutoComplete(); // Your existing initialization
    }

    @Override
    public void onPause() {
        super.onPause();
        if (expenseCategoryInReviewYourExpensesViewAutoCompleteTextView != null) {
            expenseCategoryInReviewYourExpensesViewAutoCompleteTextView.setText("");
            expenseCategoryInReviewYourExpensesViewAutoCompleteTextView.clearFocus();
        }
        if (textInputLayoutForCategory != null) {
            textInputLayoutForCategory.setHint("Select a category");
        }
    }


    /**
     * <body style="color:white;">
     * Este metodo se encarga de limpiar y restaurar los campos visuales asociados a la interfaz del
     * fragmento antes de abandonarlo o realizar otras actualizaciones.
     *
     * <p>Realiza las siguientes acciones principales:</p>
     * <ul>
     *   <li>Resetea el texto del {@link AutoCompleteTextView} a un valor vacio para evitar que
     *   se conserve informacion anterior.</li>
     *   <li>Restablece el hint del {@link TextInputLayout} a "Select a category", asegurando la
     *   consistencia visual en la UI.</li>
     *   <li>Elimina todas las vistas de {@link LinearLayout} relacionadas con los gastos,
     *   para limpiar elementos visuales previamente renderizados.</li>
     * </ul>
     *
     * <p>El metodo utiliza conceptos de manipulacion de vistas y referencias a los componentes
     * de interfaz. Asegura que todos los cambios no deriven en inconsistencias cuando el usuario
     * abandone la vista actual.</p>
     *
     * @throws NullPointerException Si algun componente visual especificado no fue inicializado
     *                              correctamente antes de llamar al metodo.
     *                              </body>
     */
    private void clearFieldsBeforeLeaving() {
        if (this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView != null) {
            this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView.setText("");
            if (this.textInputLayoutForCategory != null) {
                this.expenseCategoryInReviewYourExpensesViewAutoCompleteTextView.setText("");
                this.textInputLayoutForCategory.setHint("Select a category");
            }
        }
        if (reviewYourExpensesExpensesLinearLayout != null) {
            reviewYourExpensesExpensesLinearLayout.removeAllViewsInLayout();
        }

    }
}



