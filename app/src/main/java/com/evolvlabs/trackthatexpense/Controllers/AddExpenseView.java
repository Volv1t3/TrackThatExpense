package com.evolvlabs.trackthatexpense.Controllers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import com.google.android.material.R.drawable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.time.Instant;
import java.util.List;
import java.util.Locale;


/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo incluye la implementacion de un controller para el fragmento
 * relacionado con el ingreso de expenses en la aplicacion. Para esta vista, vamos a ingresar y
 * cargar un layout especifico definido en res.layout, dentro de esta vamos a tomar cada
 * componente existente y lo vamos a analizar y usar dentro de la aplicacion.
 */
public class AddExpenseView extends Fragment {

    /*! Parametros Internos*/
    private MaterialAutoCompleteTextView addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView;
    private MaterialAutoCompleteTextView addAnExpenseViewSelectExpenseDateTextInputEditText;
    private MaterialAutoCompleteTextView addAnExpenseViewRegisterExpenseAmountTextInputEditText;
    private MaterialButton    addAnExpenseSaveExpenseMaterialButton;
    private MaterialButton    addAnExpenseCancelExpenseRegistrationMaterialButton;
    private TextInputLayout   addAnExpenseSelectExpenseCategoryTextInputLayoutManager;
    private TextInputLayout   addAnExpenseSelectExpenseDateTextInputLayoutManager;
    private TextInputLayout   addAnExpenseRegisterExpenseAmountTextInputlayoutManager;

    private ExpenseCategory expenseCategory;

    /*Este metodo nos va a permitir definir el proceso interno de la creacion de la UI, como
    trabajamos con fragmentos, no requerimos de colocar mucho codigo aqui!*/


    /**
     * <body style="color:white;">
     * Este metodo se encarga de inflar el layout definido para la vista del fragmento que
     * representa el ingreso de gastos. Utiliza el inflater proporcionado para expandir una vista
     * desde el archivo XML que contiene el diseño de la interfaz del usuario y lo retorna como un
     * objeto View. La funcionalidad del metodo se basa en los conceptos de inflado de layout en
     * Android, donde el XML de la interfaz del usuario es cargado y convertido en una estructura de
     * objetos en memoria, listo para ser usado dentro de un Fragment.
     * </body>
     *
     * @param inflater           El LayoutInflater que se utiliza para inflar cualquier vista en el
     *                           fragmento.
     * @param container          El contenedor padre al cual se anadira la interfaz. Puede ser
     *                           null.
     * @param savedInstanceState Objeto Bundle que contiene el estado guardado previamente del
     *                           fragmento, si existe. Puede ser null.
     * @return La vista inflada desde el archivo XML definido para el fragmento.
     * @throws NullPointerException Si el archivo XML para el layout no puede ser encontrado o el
     *                              inflater es nulo.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_an_expense_view,
                                container, false);
    }

    /*Aqui es donde nuestra logica de la UI se va a ejecutar generalmente*/

    /**
     * <body style="color:white;">
     * Este metodo se invoca inmediatamente despues de que la vista asociada a este fragmento ha
     * sido creada e inflada. Permite ejecutar la logica principal de inicializacion necesaria para
     * manejar los componentes de la UI y los eventos relacionados.
     * <p>
     * Conceptos clave utilizados son: 1. Conectividad: Los componentes UI del layout previamente
     * inflado son identificados utilizando sus IDs y enlazados con propiedades locales. 2.
     * Interactividad: Se asignan listeners a botones y campos para manejar eventos de usuario. 3.
     * Manejo seguro: Cualquier error durante la inicializacion es capturado y registrado en logs.
     *
     * @param view               La vista que representa la jerarquia de interfaces de usuario
     *                           infladas y que ha sido creada.
     * @param savedInstanceState Un Bundle que contiene el estado previamente guardado del Fragment,
     *                           si aplica; puede ser null.
     * @throws NullPointerException Si no se encuentran IDs de los componentes requeridos durante la
     *                              inicializacion.
     * @throws ClassCastException   Si algun componente UI tiene un tipo incorrecto en el XML.
     *                              </body>
     */
    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpMaterialButtonForMenuSelection();


        try {/*Anadimos todos los listeners necesarios luego de programaticamente cargar los
        componentes*/
            this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView =
                    (MaterialAutoCompleteTextView) this
                    .getView()
                    .findViewById(R.id.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView);
            this.addAnExpenseViewSelectExpenseDateTextInputEditText = (MaterialAutoCompleteTextView) this
                    .getView()
                    .findViewById(R.id.addAnExpenseViewSelectExpenseDateTextInputEditText);
            this.addAnExpenseViewRegisterExpenseAmountTextInputEditText = (MaterialAutoCompleteTextView) this
                    .getView()
                    .findViewById(R.id.addAnExpenseViewRegisterExpenseAmountTextInputEditText);
            this.addAnExpenseSaveExpenseMaterialButton = (MaterialButton) this
                    .getView()
                    .findViewById(R.id.addAnExpenseSaveExpenseMaterialButton);
            this.addAnExpenseCancelExpenseRegistrationMaterialButton = (MaterialButton) this
                    .getView()
                    .findViewById(R.id.addAnExpenseCancelExpenseRegistrationMaterialButton);

            this.addAnExpenseSelectExpenseCategoryTextInputLayoutManager = (TextInputLayout) this
                    .getView()
                    .findViewById(R.id.addAnExpenseSelectExpenseCategoryTextInputLayoutManager);
            this.addAnExpenseSelectExpenseDateTextInputLayoutManager = (TextInputLayout) this
                    .getView()
                    .findViewById(R.id.addAnExpenseSelectExpenseDateTextInputLayoutManager);
            this.addAnExpenseRegisterExpenseAmountTextInputlayoutManager = (TextInputLayout) this
                    .getView()
                    .findViewById(R.id.addAnExpenseRegisterExpenseAmountTextInputlayoutManager);

        } catch (Exception e) {
            Log.e("AddExpenseView", "Error al cargar los componentes de la UI: " + e.getMessage());
        }

        setUpDatePicker();
        setUpButtonsForInteraction();
        setUpInputListenersForAllFields();
        setUpExpenseCategoryDropdown();

    }

    /**
     * <body style="color:white;">
     * Este metodo configura un boton de tipo {@link MaterialButton} para habilitar la interaccion
     * del menu lateral deslizante del DrawerLayout. El metodo identifica el boton en la vista
     * inflada actual y le asigna un listener de clic. En caso de ser presionado, el menu lateral
     * izquierdo se abre utilizando {@link GravityCompat.START}.
     * <p>
     * Conceptos clave:
     * <ol>
     *     <li>Identificacion del componente en la vista mediante su ID.</li>
     *     <li>Configuracion de interactividad a traves de {@link View.OnClickListener}.</li>
     *     <li>Uso del patron de diseño DrawerLayout para navegacion en la UI.</li>
     * </ol>
     * </p>
     * </body>
     *
     * @throws NullPointerException Si {@link View#getView()} es nulo o no se encuentra el
     *                              {@link MaterialButton} con el ID especificado.
     */
    private void setUpMaterialButtonForMenuSelection() {
        MaterialButton menuButton = this.getView().findViewById(R.id.menuButtonForNavigation);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }
    }

    /**
     * <body style="color:white;">
     * Este metodo inicializa listeners para los campos de entrada dentro del fragmento. Se utiliza
     * principalmente para limpiar los hints contextuales (ayuda) del componente
     * {@link TextInputLayout} cuando estos campos obtienen foco. Esto permite mantener una interfaz
     * de usuario limpia y legible al evitar que el usuario escriba superponiendose sobre el texto
     * de ayuda preexistente.
     * <p>
     * Conceptos clave:
     * <ol>
     *     <li>
     *         Enlaces entre componentes de UI y logica programatica utilizando
     *         {@link View.OnFocusChangeListener}.
     *     </li>
     *     <li>
     *         Manejo de eventos de foco para mejorar la interaccion de usuario.
     *     </li>
     * </ol>
     * </p>
     * </body>
     *
     * @throws NullPointerException Si alguna referencia interna al componente es nula durante la
     *                              vinculacion de los listeners.
     */
    private void setUpInputListenersForAllFields() {
        //? 1. Para manejar el limpiado de los inputs de todos los campos, tenemos que manejar el
        // TextInputLayout que hace un wrap al campo, esto nos sirve como en los ejemplos del
        // libro para tener un texto sobrepuesto como ayuda, pero al momento de entrar tenemos
        // que limpiarlo porque si no el usuario escribe debajo y eso no se alcanza a leer.

        this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            addAnExpenseSelectExpenseCategoryTextInputLayoutManager.setHint("");
                        }
                    }
                });

        this.addAnExpenseViewSelectExpenseDateTextInputEditText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            addAnExpenseSelectExpenseDateTextInputLayoutManager.setHint("");
                        }
                    }
                });
        this.addAnExpenseViewRegisterExpenseAmountTextInputEditText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            addAnExpenseRegisterExpenseAmountTextInputlayoutManager.setHint("");
                        }
                    }
                });
    }


    /**
     * <body style="color:white;">
     * Este metodo configura el campo desplegable para seleccionar la categoria del gasto en el
     * formulario. Utiliza el {@link ApplicationDataPOJO} para obtener la lista de categorias y
     * mostrarlas al usuario en un {@link ListPopupWindow}. El metodo permite interactuar con las
     * categorias visualizadas y seleccionar una de ellas para asociarla a un gasto.
     * <p>
     * Conceptos clave utilizados:
     * <ol>
     *     <li>Uso del {@link ApplicationDataPOJO} para obtener datos unificados dentro de un patrón Singleton.</li>
     *     <li>{@link ArrayAdapter} para enlazar una lista de datos con una interfaz visual.</li>
     *     <li>{@link ListPopupWindow} para mostrar elementos en una lista desplegable independiente.</li>
     *     <li>Eventos {@link ListPopupWindow.OnItemClickListener} para manejar la seleccion de
     *         una categoria.</li>
     * </ol>
     * </p>
     * <p>
     * Manejo de errores y excepciones:
     * <ul>
     *     <li>El metodo muestra un mensaje de error (mediante toast) si no se encuentran
     *         categorias disponibles para mostrar en la lista.</li>
     * </ul>
     * </p>
     *
     * @throws NullPointerException  Si algun componente requerido para desplegar la lista seria
     *                               nulo.
     * @throws IllegalStateException Si ocurre un error al acceder al singleton
     *                               {@link ApplicationDataPOJO}.
     *                               </body>
     */
    private void setUpExpenseCategoryDropdown() {
        /*
         * La idea de esta seccion, es definir un metodo que nos permita agarrar el contenido
         * de los campos del Singleton de las expenses, y usar los nombres de los campos como
         * objetos de la lista (visuales) e internamente registrar en un estado en base a ese
         * objeto el actual Category object con lo que podemos anadir y manejar el gasto!*/

        this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView.setOnClickListener(v -> {
            List<ExpenseCategory> categories = ApplicationDataPOJO
                    .getApplicationDataPOJOInstance()
                    .getApplicationDataPojoExpenseCategories();

            if (categories.isEmpty()) {
                createAndShowToast(getContext(),
                                   "No expense categories found",
                                   R.layout.custom_toast_for_errors);
                return;
            }

            ArrayAdapter<ExpenseCategory> adapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.drop_down_menu_for_user_categories,
                    categories
            );

            ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());
            listPopupWindow.setAdapter(adapter);
            listPopupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                                                                              drawable.m3_tabs_transparent_background
                    , null));
            listPopupWindow.setAnchorView(this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView);
            listPopupWindow.setModal(true);

            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                expenseCategory = adapter.getItem(position);
                this.addAnExpenseSelectExpenseCategoryTextInputLayoutManager.setHint("");
                this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView.setText(expenseCategory.getExpenseCategoryName());
                listPopupWindow.dismiss();
            });

            listPopupWindow.show();
        });

    }


    /**
     * <body style="color:white;">
     * Este metodo configura los listeners de click en los botones principales de interaccion dentro
     * del fragmento. Implementa las acciones para:
     * <ul>
     *     <li>
     *         Guardar un gasto nuevo, validando que todos los campos obligatorios contengan
     *         informacion; en caso de exito, registra el gasto en el objeto {@link ApplicationDataPOJO}.
     *     </li>
     *     <li>
     *         Cancelar el registro de un gasto, limpiando los campos de entrada en el formulario
     *         y reposicionando los hints de ayuda en los mismos.
     *     </li>
     * </ul>
     * <p>
     * Concepts utilizados:
     * <ul>
     *     <li>Validacion de entradas del usuario en el formulario de ingreso de gastos.</li>
     *     <li>Uso del patron Singleton para gestionar datos globales de la aplicacion con {@link ApplicationDataPOJO}.</li>
     *     <li>Manejo de errores mediante mensajes personalizados al usuario usando {@link Toast}.</li>
     * </ul>
     * </p>
     * <p>
     * Manejo de errores y excepciones:
     * <ul>
     *     <li>
     *         El metodo utiliza {@link #createAndShowToast(Context, String, Integer)} para mostrar
     *         errores y advertencias al usuario en tiempo de ejecucion.
     *     </li>
     * </ul>
     * </p>
     * </body>
     *
     * @throws NullPointerException Si algun componente requerido de la vista es nulo al momento de
     *                              intentar enlazar los listeners.
     */
    private void setUpButtonsForInteraction() {
        this.addAnExpenseSaveExpenseMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //! Paso Base: tomamos un boolean de la existencia de un componente en cada campo
                // de la UI
                boolean isCategorySelected = expenseCategory != null;
                boolean isDateSelected = selectedDate != null;
                boolean isAmountEntered =
                        !addAnExpenseViewRegisterExpenseAmountTextInputEditText
                        .getText().toString().trim().isEmpty();
                //? 1. Revisamos si tenemos una categoria seleccionada
                if (isCategorySelected){
                    //? 1.A Revisamos si existe una fecha seleccionada
                    if (isDateSelected){
                        //? 2.A Revisamos si hay una cantidad definida
                        if (isAmountEntered){
                            //? 3.A Tomamos los valores de todos lo campos
                            String amount = addAnExpenseViewRegisterExpenseAmountTextInputEditText
                                    .getText().toString().trim();
                            BigDecimal amountBigDecimal = new BigDecimal(amount);
                            //? 4.A Revisamos si la cantidad ingresada por el usuario es mayor
                            // que cero
                            if (amountBigDecimal.compareTo(BigDecimal.ZERO) < 0){
                                //? 4.A.1 Si es menor que cero, mostramos un tip
                                createAndShowToast(getContext(),
                                                   "Amount must be greater than zero",
                                                   R.layout.custom_toast_for_errors);
                            } else if (expenseCategory.getExpenseCategoryMaxPerExpenseValue()
                                    .compareTo(amountBigDecimal) < 0){
                                //? 4.A.2 Revisamos si el valor ingresado es mayor que el limite
                                // de gato por gasto en la categori
                                createAndShowToast(getContext(),
                                                   "Amount must be less than or equal to " +
                                                   expenseCategory.getExpenseCategoryMaxPerExpenseValue(),
                                                   R.layout.custom_toast_for_errors);
                            }
                            //? 4.A Validamos que la cantidad de dinero corresponda a un valor
                            // menor que la cantidad de ingreso de la categoria seleccionada
                            else {
                                //? 5.A Creamos el gasto
                                Expense expense = new Expense(expenseCategory.getExpenseCategoryName(),
                                                              selectedDate,
                                                              amountBigDecimal);
                                //? 6.A Guardamos el gasto en el Singleton
                                ApplicationDataPOJO.getApplicationDataPOJOInstance()
                                        .addExpenseIntoExpenseMap(expense);
                                //? 7.A Limpiamos los campos
                                clearFields();
                                //? 8.A Limpiams toda marca focused para poder poner las hints de
                                // nuevo
                                positionHintsAgain();
                                //? 8.A Mostramos un tip al usuario
                               createAndShowToast(getContext(),
                                                  "Expense successfully added",
                                                  R.layout.custom_toast_for_success);
                            }
                        } else if (Date.from(Instant.now()).compareTo(selectedDate) < 0){
                            //? 3.B Enviamos un Tip al usario informandole que tiene que seleccionar un
                            // componente de la lista
                            createAndShowToast(getContext(),
                                               "A Date in the future must not be entered " +
                                                       "when " +
                            "attempting to insert an expense.", R.layout.custom_toast_for_errors);
                        }
                        else {
                            //? 3.B Enviamos un Tip al usario informandole que tiene que
                            // seleccionar un
                            // componente de la lista
                            createAndShowToast(getContext(),
                                               "An Amount must be entered before attempting " +
                            "to insert an expense.", R.layout.custom_toast_for_errors);
                        }
                    }
                    else {
                        //? 2.B Enviamos un Tip al usario informandole que tiene que seleccionar un
                        // componente de la lista
                        createAndShowToast(getContext(),
                                           "A Date must be selected before attempting " +
                            "to insert an expense.", R.layout.custom_toast_for_errors);
                    }
                } else {
                    //? 1.B Enviamos un Tip al usario informandole que tiene que seleccionar un
                    // componente de la lista
                    createAndShowToast(getContext(),
                                       "A Category must be selected before attempting " +
                            "to insert an expense.", R.layout.custom_toast_for_errors);
                }
            }
        });
        this.addAnExpenseCancelExpenseRegistrationMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                positionHintsAgain();
            }
        });
    }


    private Date selectedDate;

    /**
     * <body style="color:white;">
     * Este metodo configura un listener para que el usuario pueda seleccionar una fecha dentro del
     * campo de entrada correspondiente a la fecha de un gasto. Utiliza un {@link DatePickerDialog}
     * para mostrar un calendario interactivo, permitir la seleccion y actualizar el campo
     * visualmente con la fecha seleccionada.
     * <p>
     * Conceptos clave utilizados:
     * <ol>
     *     <li>
     *         Creacion y configuracion de un dialogo de tipo DatePickerDialog para seleccionar fechas.
     *     </li>
     *     <li>
     *         Manejo de eventos para capturar la fecha seleccionada a traves de un callback y
     *         actualizar su valor en el campo correspondiente.
     *     </li>
     *     <li>
     *         Formateo y presentacion de la fecha seleccionada en un formato legible para el usuario.
     *     </li>
     * </ol>
     * </p>
     * <p>
     * Manejo de errores y excepciones:
     * <ul>
     *     <li>
     *         {@link NullPointerException} puede ser lanzada si algun componente de interfaz
     *         que se intenta referenciar no esta inicializado correctamente.
     *     </li>
     * </ul>
     * <p>
     * Este metodo no tiene parametros de entrada ni valor de retorno.
     * </p>
     *
     * @throws NullPointerException Si algun componente necesario no esta inicializado.
     *                              </body>
     */
    private void setUpDatePicker() {
        this.addAnExpenseViewSelectExpenseDateTextInputEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        selectedDate = selectedCalendar.getTime();

                        String displayDate = String.format(Locale.getDefault(),
                                                           "%02d/%02d/%d", 
                                                           selectedDay, 
                                                           selectedMonth + 1, 
                                                           selectedYear);
                        //? 1. Actualizamos el campo de texto con la fecha seleccionada
                        this.addAnExpenseSelectExpenseDateTextInputLayoutManager.setHint("");
                        addAnExpenseViewSelectExpenseDateTextInputEditText.setText(displayDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        clearFields();
        positionHintsAgain();
        setUpMaterialButtonForMenuSelection();
        setUpExpenseCategoryDropdown();
        setUpButtonsForInteraction();
        setUpDatePicker();
    }

    @Override
    public void onPause() {
        super.onPause();
        clearFields();
        positionHintsAgain();
    }


    /**
     * <body style="color:white;">
     * Este metodo reinicia el valor de todos los campos de entrada del formulario de registro de
     * gastos. Se asegura de limpiar cualquier texto contenido dentro de los campos visibles para el
     * usuario, dejandolos en un estado inicial. Este comportamiento es util para prevenir datos
     * previos no eliminados y proporcionar una experiencia limpia al usuario.
     *
     * <p>
     * Conceptos clave utilizados:
     * <ol>
     *     <li>Reinicio programatico: Cada elemento UI es referenciado y su contenido es purgado utilizando el
     *         metodo {@link android.widget.TextView#setText(CharSequence)}.</li>
     *     <li>Manejo de campos dependientes del contexto visual: Asegura que los elementos editables en pantalla
     *         queden vacios, adaptados al flujo de interaccion del usuario en la aplicacion.</li>
     * </ol>
     * </p>
     * <p>
     * Este metodo no requiere parametros de entrada ni devuelve un valor.
     * </p>
     *
     * @throws NullPointerException Si algun campo o componente requerido no fue inicializado
     *                              correctamente antes de llamarlo.
     *                              </body>
     */
    private void clearFields() {
        this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView.setText("");
        this.addAnExpenseViewRegisterExpenseAmountTextInputEditText.setText("");
        this.addAnExpenseViewSelectExpenseDateTextInputEditText.setText("");
    }

    /**
     * <body style="color:white;">
     * Este metodo posiciona de nuevo los hints predeterminados del formulario de registro de gastos
     * si los campos no tienen el foco activo. Esta funcionalidad asegura que en caso de que un
     * campo pierda el foco y no tenga texto de entrada, su hint aparezca como texto orientativo
     * para el usuario.
     *
     * <p>
     * Conceptos clave que utiliza:
     * <ol>
     *     <li>Validacion de estado activo/inactivo del foco en los campos de entrada utilizando
     *         {@link android.view.View#isFocused()}.</li>
     *     <li>Restablecimiento de texto explicativo (hint) en los componentes
     *         {@link com.google.android.material.textfield.TextInputLayout} para mantener una
     *         interfaz clara y comprensible para el usuario.</li>
     * </ol>
     * </p>
     *
     * <p>
     * Este metodo no toma parametros de entrada ni devuelve un valor. Es invocado durante el ciclo
     * de vida del Fragment para mantener un estado consistente en los campos de la UI.
     * </p>
     *
     * @throws NullPointerException Si alguno de los campos de entrada o layouts asociados no esta
     *                              inicializado previamente.
     *                              </body>
     */
    private void positionHintsAgain() {
        if (!this.addAnExpenseViewSelectExpenseCategoryAutoCompleteTextView.isFocused()) {
            this.addAnExpenseSelectExpenseCategoryTextInputLayoutManager.setHint("Food & Drink");
        }
        if (!this.addAnExpenseViewSelectExpenseDateTextInputEditText.isFocused()) {
            this.addAnExpenseSelectExpenseDateTextInputLayoutManager.setHint("April 22, 2004");
        }
        if (!this.addAnExpenseViewRegisterExpenseAmountTextInputEditText.isFocused()) {
            this.addAnExpenseRegisterExpenseAmountTextInputlayoutManager.setHint("$300");
        }
    }


    /**
     * <body style="color:white;">
     * Este metodo genera y muestra un mensaje personalizado tipo {@link Toast} que se infla
     * utilizando un diseño predefinido. Permite al usuario presentar mensajes estilizados con la
     * posibilidad de seleccionar un layout unico desde la carpeta drawable.
     *
     * <p>
     * Conceptos clave que utiliza:
     *  <ol>
     *      <li>Inflado de vistas para cargar un diseño visual especifico desde un archivo XML.</li>
     *      <li>Configuracion de gravedad y duracion para el posicionamiento y tiempo visible del mensaje.</li>
     *      <li>Uso del componente {@link Toast} para mostrar mensajes transitorios al usuario.</li>
     *  </ol>
     * </p>
     *
     * @param contextPassedFromToastCreator Contexto necesario para la creacion del {@link Toast}.
     *                                      Es usado para inflar la vista y generar el mensaje.
     * @param toastMessage                  Mensaje que sera mostrado dentro de la vista del
     *                                      {@link Toast}.
     * @param drawableID                    Identificador del recurso que contiene el diseño del
     *                                      layout a ser inflado como contenedor visual del mensaje.
     *                                      Este recurso debe estar definido en drawable.
     * @throws NullPointerException Si algun parametro requerido (contexto, mensaje o ID del
     *                              drawable) es nulo o invalido.
     *                              </body>
     */
    public static void createAndShowToast(Context contextPassedFromToastCreator,
                                          String toastMessage,
                                          Integer drawableID) {

        //? 1. Creamos el nuevo toast, basado en nuestro layout para un toast generado en la
        // carpeta drawable. La idea es que tenemos que inflar este layout dentro de la
        // aplicacion para que se carge con sus componentes!
        LayoutInflater inflaterForToast = LayoutInflater.from(contextPassedFromToastCreator);
        View layoutForToast = inflaterForToast.inflate(drawableID,
                                                       null);

        //? 2. Conectamos el mensaje con el texto definido
        TextView textViewForToastText = layoutForToast.findViewById(R.id.toastText);
        textViewForToastText.setText(toastMessage);

        //?3. Cargamos la vista y lo mostramos
        Toast toastViewFinal = new Toast(contextPassedFromToastCreator);
        toastViewFinal.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50);
        toastViewFinal.setDuration(Toast.LENGTH_SHORT);
        toastViewFinal.setView(layoutForToast);
        toastViewFinal.show();
    }


}
