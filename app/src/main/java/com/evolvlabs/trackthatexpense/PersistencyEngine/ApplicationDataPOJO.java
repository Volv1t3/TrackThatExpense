package com.evolvlabs.trackthatexpense.PersistencyEngine;


import android.util.Log;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo contiene una serie de metodos de ayuda para el manejo de la
 * informacion producida durante la ejecucion de la aplicacion, la informacion contenida en esta
 * clase nos permite manejar la carga y descarga de datos correctamente, admitiendo el manejo de
 * una instancia de la clase compartida dentro del programa
 */
public class ApplicationDataPOJO {


    /*! Parametros de la clase*/
    
    public static ApplicationDataPOJO applicationDataPOJOInstance;
    private List<ExpenseCategory> applicationDataPojoExpenseCategories;
    private Map<String, List<Expense>> applicationDataPojoExpensesMap;


    /**
     * Proporciona la instancia compartida de la clase ApplicationDataPOJO. Este metodo implementa
     * el patron Singleton para garantizar que solo haya una instancia de esta clase en ejecucion
     * durante toda la aplicacion.
     *
     * <p>Si la instancia ya ha sido creada, se devuelve directamente. Si no, se crea
     * una nueva instancia utilizando el constructor privado y posteriormente la almacena.
     *
     * @return la unica instancia compartida de ApplicationDataPOJO.
     */
    public static ApplicationDataPOJO getApplicationDataPOJOInstance() {
        if (applicationDataPOJOInstance == null) {
            applicationDataPOJOInstance = new ApplicationDataPOJO();
        }

        return applicationDataPOJOInstance;
    }


    /**
     * Constructor privado de la clase ApplicationDataPOJO.
     *
     * <p>Inicializa las listas y mapas internos con datos predeterminados para las categorias
     * de gastos y sus transacciones. Este constructor utiliza varias estructuras de datos como
     * {@link List} y {@link Map} para almacenar informacion relacionada con los gastos registrados
     * y organizarlos segun sus categorias.</p>
     *
     * <p>Para las categorias iniciales, establece valores base para presupuestos como
     * "Groceries", "Transportation", "Entertainment" y "Healthcare". Adicionalmente, agrega dos
     * transacciones simuladas a cada categoria utilizando la fecha actual como referencia.</p>
     *
     * <p>Al ser privado, este constructor es accesible unicamente desde la propia clase,
     * reforzando el cumplimiento del patron Singleton.</p>
     */
    private ApplicationDataPOJO() {
        this.applicationDataPojoExpenseCategories = new ArrayList<>();
        this.applicationDataPojoExpenseCategories
                .add(new ExpenseCategory("Groceries",
                     BigDecimal.valueOf(500l),
                     BigDecimal.valueOf(5000l)));
        this.applicationDataPojoExpenseCategories
                .add(new ExpenseCategory("Transportation",
                     BigDecimal.valueOf(150l),
                     BigDecimal.valueOf(2000l)));
        this.applicationDataPojoExpenseCategories
                .add(new ExpenseCategory("Entertainment",
                     BigDecimal.valueOf(500l),
                     BigDecimal.valueOf(1000l)));
        this.applicationDataPojoExpenseCategories
                .add(new ExpenseCategory("Healthcare",
                     BigDecimal.valueOf(500l),
                     BigDecimal.valueOf(2500l)));
        this.applicationDataPojoExpensesMap = new HashMap<>();

        for (ExpenseCategory category : this.applicationDataPojoExpenseCategories) {
            List<Expense> expenses = new ArrayList<>();
            expenses.add(new Expense(category.getExpenseCategoryName(),
                         Date.from(Instant.now()),
                                     BigDecimal.valueOf(200)));
            expenses.add(new Expense(category.getExpenseCategoryName(),
                         Date.from(Instant.now()),
                                     BigDecimal.valueOf(350)));
            this.applicationDataPojoExpensesMap.put(category.getExpenseCategoryName(), expenses);
        }


    }

    /*! Setters y Getters*/

    /**
     * Este metodo establece la lista de categorias de gastos utilizadas en la aplicacion.
     *
     * <p>El metodo reemplaza la lista actual de categorias de gastos con la proporcionada
     * como parametro. Como medida de seguridad, valida que el parametro no sea nulo antes de
     * asignarlo para evitar errores en tiempo de ejecucion. Si la lista proporcionada es nula, se
     * genera y registra una excepcion {@link NullPointerException}.</p>
     *
     * @param expenseCategories una lista de objetos {@link ExpenseCategory} que representan las
     *                          categorias de gastos que se quieren establecer en la aplicacion.
     * @throws NullPointerException si el parametro {@code expenseCategories} es nulo.
     */
    public void setApplicationDataPojoExpenseCategories(List<ExpenseCategory> expenseCategories)
            throws NullPointerException {
        if (expenseCategories == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expenseCategories no puede ser " +
                    "nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expenseCategories no puede ser nulo");
        }

        this.applicationDataPojoExpenseCategories = expenseCategories;
    }

    /**
     * <body style="color:white;">
     * Obtiene la lista de categorias de gastos utilizadas en la aplicacion.
     *
     * <p>
     * Este metodo devuelve una lista de objetos {@link ExpenseCategory}, la cual representa las
     * distintas categorias que el usuario ha registrado en la aplicacion, como "Groceries",
     * "Transportation", entre otras. La lista es almacenada internamente en una estructura
     * {@link List}, que permite el acceso secuencial y manipular las categorias segun sea
     * necesario.
     * </p>
     *
     * @return Una lista de objetos {@link ExpenseCategory} que representan las categorias de gastos
     * actuales registradas en la aplicacion.
     * </body>
     */
    public List<ExpenseCategory> getApplicationDataPojoExpenseCategories() {
        return applicationDataPojoExpenseCategories;
    }

    /**
     * <body style="color:white;">
     * Establece el mapa de gastos utilizados en la aplicacion.
     *
     * <p>
     * Este metodo permite reemplazar el mapa actual que almacena los gastos con uno nuevo. El mapa
     * relaciona las categorias de gastos, representadas como cadenas {@link String}, con sus
     * respectivas listas de gastos {@link Expense}. Si el parametro {@code expensesMap}
     * proporcionado es nulo, se lanza una excepcion {@link NullPointerException} para garantizar
     * que la integridad del objeto <i>Singleton</i> sea mantenida.
     * </p>
     *
     * @param expensesMap Un {@link Map} con claves de tipo {@link String} que representan las
     *                    categorias de gastos, y valores de tipo {@link List} que representan los
     *                    gastos asociados a cada categoria.
     * @throws NullPointerException si el parametro {@code expensesMap} es nulo.
     *                              </body>
     */
    public void setApplicationDataPojoExpensesMap(Map<String, List<Expense>> expensesMap) {
        if (expensesMap == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expensesMap no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expensesMap no puede ser nulo");
        }

        this.applicationDataPojoExpensesMap = expensesMap;
    }

    /**
     * <body style="color:white;">
     * Retorna el mapa de todas las categorias de gastos y sus gastos asociados.
     *
     * <p>
     * Este metodo devuelve un {@link Map} donde las claves son cadenas {@link String} que
     * representan el nombre de cada categoria de gasto, y los valores son listas {@link List} de
     * objetos {@link Expense} asociados a dichas categorias. Si no se han registrado gastos en una
     * categoria, sera devuelta una lista vacia.
     * </p>
     *
     * @return Un mapa {@link Map} que relaciona nombres de categorias de gastos con sus respectivas
     * listas de transacciones.
     * </body>
     */
    public Map<String, List<Expense>> getApplicationDataPojoExpensesMap() {
        return this.applicationDataPojoExpensesMap;
    }

    /*! Metodos requeridos para trabajo interno*/


    /**
     * <body style="color:white;">
     * Agrega una nueva categoria de gasto a la lista interna de categorias.
     *
     * <p>Este metodo se asegura de que no se pueda agregar una categoria nula o vacia a la lista.
     * Si el parametro {@code expenseCategory} es nulo, se lanza una excepcion
     * {@link NullPointerException}. Tambien valida que la lista interna de categorias ya este
     * inicializada, dado que la clase sigue el patron Singleton y dicha lista deberia haberse
     * creado al instanciarse.</p>
     *
     * <p>Antes de agregar la nueva categoria, compara el nombre de la categoria con las categorias
     * existentes para evitar duplicados. Si la categoria ya existe, el metodo regresa
     * inmediatamente con un valor {@code false}. De lo contrario, agrega la nueva categoria a la
     * lista interna y crea una entrada vacia en el mapa interno asociado a los gastos de esa
     * categoria.</p>
     *
     * @param expenseCategory Un objeto de tipo {@link ExpenseCategory} que representa la nueva
     *                        categoria a agregar, con nombre y limites de presupuesto definidos.
     * @return {@code true} si la categoria es agregada correctamente; {@code false} si la categoria
     * ya existia.
     * @throws NullPointerException si el parametro es nulo o si el nombre de la categoria esta
     *                              vacio.
     *                              </body>
     */
    public final boolean addCategoryIntoCategoryList(ExpenseCategory expenseCategory)
            throws NullPointerException {
        /*Revisiond e estado externo, si la categoria es nula o vacia*/
        if (expenseCategory == null || expenseCategory.getExpenseCategoryName().isEmpty()) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expenseCategory no puede ser " +
                    "nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expenseCategory no puede ser nulo");
        }
        /*Revision de estado interno, si se llama antes de tener una instancia*/
        if (this.applicationDataPojoExpenseCategories == null) {
            Log.e("Fatal Error Code 0x0000 [Raised]", "Se ha intentado acceder a un parametro nulo" +
                    " en un singleton, error de estado");
            return false;
        }

        boolean categoriaYaIngresada = this.applicationDataPojoExpenseCategories
                .stream()
                .anyMatch(new Predicate<ExpenseCategory>() {
                    @Override
                    public boolean test(ExpenseCategory expenseCategory2) {
                        return expenseCategory2.getExpenseCategoryName()
                                .equals(expenseCategory.getExpenseCategoryName());
                    }
                });

        if (categoriaYaIngresada) {
            /*Salimos rapido si ya existe!*/
            return false;
        }

        this.applicationDataPojoExpenseCategories.add(expenseCategory);
        this.applicationDataPojoExpensesMap.put(expenseCategory.getExpenseCategoryName(),
                                                new ArrayList<>());
        return true;
    }


    /**
     * <body style="color:white;">
     * Remueve una categoria de gasto de la lista interna de categorias y del mapa correspondiente.
     *
     * <p>
     * Este metodo permite eliminar una categoria de gasto especifica de la lista interna
     * {@link List} en la clase, asi como del {@link Map} que vincula las categorias con los gastos
     * asociados. Si la categoria no se encuentra en la lista, no se realiza ninguna accion y el
     * metodo devuelve {@code false}. En caso de encontrarla, tanto la categoria como sus gastos
     * asociados se eliminan.
     * </p>
     *
     * <p>
     * Internamente, utiliza la funcionalidad {@link List#removeIf(Predicate)} para buscar y
     * eliminar la categoria por su nombre. Si la eliminacion es exitosa, tambien elimina la entrada
     * correspondiente al mapa utilizando {@link Map#remove(Object)}.
     * </p>
     *
     * @param expenseCategory Un objeto de tipo {@link ExpenseCategory} que representa la categoria
     *                        de gasto a eliminar.
     * @return {@code true} si la categoria fue eliminada de la lista interna; {@code false} si no
     * se encontro la categoria o no se pudo eliminar.
     * @throws NullPointerException si el parametro {@code expenseCategory} es nulo o si el nombre
     *                              de la categoria es una cadena vacia.
     *                              </body>
     */
    public final boolean removeCategoryFromCategoryList(ExpenseCategory expenseCategory) {
        if (expenseCategory == null || expenseCategory.getExpenseCategoryName().isEmpty()) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expenseCategory no puede ser " +
                    "nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expenseCategory no puede ser nulo");
        }

        if (this.applicationDataPojoExpenseCategories == null) {
            Log.e("Fatal Error Code 0x0000 [Raised]", "Se ha intentado acceder a un parametro nulo" +
                    " en un singleton, error de estado");
            return false;
        }

        boolean categoriaRemovidaDeListadoInterno = this.applicationDataPojoExpenseCategories
                .removeIf(new Predicate<ExpenseCategory>() {
                    @Override
                    public boolean test(ExpenseCategory expenseCategory2) {
                        return expenseCategory2.getExpenseCategoryName()
                                .equals(expenseCategory.getExpenseCategoryName());
                    }
                });

        if (categoriaRemovidaDeListadoInterno) {
            /*Elimminamos tambien la instancia del mapa*/
            this.applicationDataPojoExpensesMap.remove(expenseCategory.getExpenseCategoryName());
        }

        return categoriaRemovidaDeListadoInterno;
    }


    /**
     * <body style="color:white;">
     * Agrega un gasto a la estructura interna del mapa de categorias de gastos.
     *
     * <p>Este metodo permite asociar una nueva transaccion de gasto con su respectiva
     * categoria {@link ExpenseCategory}. Si la categoria ya existe dentro del mapa, el gasto es
     * anadido a la lista asociada. Si la categoria no existe, crea una nueva asociacion en el mapa
     * inicializando la lista correspondiente.</p>
     *
     * <p>Para manejar posibles errores, el metodo valida que:</p>
     * <ul>
     *   <li>La instancia de {@link Expense} proporcionada no sea {@code null}. De ser asi,
     *   se lanza una excepcion {@link NullPointerException}.</li>
     *   <li>El mapa interno de gastos no se encuentre en un estado nulo.</li>
     * </ul>
     *
     * <p>El mapa utiliza un metodo de recuperacion con valor predeterminado
     * {@link Map#getOrDefault(Object, Object)} para determinar si una categoria ya cuenta
     * con una lista de gastos asociada. El metodo verifica si dicha lista existe
     * en el mapa y, de no ser asi, la inicializa con el nuevo gasto.</p>
     *
     * @param expense el objeto expense a anadir en el mapa, el cual incluye informacion como su
     *                categoria {@link Expense#getExpenseCategory()}.
     * @return {@code true} si el objeto fue agregado correctamente; {@code false} si la operacion
     * no pudo completarse.
     * @throws NullPointerException si el gasto ({@code expense}) proporcionado es {@code null}.
     *                              </body>
     */
    public final boolean addExpenseIntoExpenseMap(Expense expense) throws NullPointerException {
        if (expense == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expense no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expense no puede ser nulo");
        }

        if (this.applicationDataPojoExpensesMap == null) {
            Log.e("Fatal Error Code 0x0000 [Raised]", "Se ha intentado acceder a un parametro nulo" +
                    " en un singleton, error de estado");
            return false;
        }


        List<Expense> categoryExpenses =
                this.applicationDataPojoExpensesMap.getOrDefault(expense.getExpenseCategory(),
                                                                 new ArrayList<>());
        if (categoryExpenses.isEmpty()) {
            this.applicationDataPojoExpensesMap.put(expense.getExpenseCategory(),
                                                    new ArrayList<>() {{
                                                        add(expense);
                                                    }});
            return true;
        }
        return categoryExpenses.add(expense);

    }


    /**
     * <body style="color:white;">
     * Elimina un gasto especifico del mapa interno de gastos categorizados.
     *
     * <p>
     * Este metodo verifica si el gasto proporcionado como parametro {@link Expense} pertenece a la
     * categoria correspondiente en el mapa interno {@link Map}. Si existe dentro de la lista de
     * gastos de dicha categoria, el metodo procede a eliminar el gasto. En caso contrario, devuelve
     * {@code false} sin realizar cambios.
     * </p>
     *
     * <p>
     * Este metodo sigue un enfoque seguro asegurandose de comprobar si:
     * </p>
     * <ul>
     *   <li>El parametro {@code expense} no es {@code null}. De no cumplirse, se lanza una
     *   excepcion {@link NullPointerException}.</li>
     *   <li>La estructura interna del mapa no es {@code null}.</li>
     *   <li>La categoria vinculada al objeto {@code expense} existe en la estructura interna.</li>
     * </ul>
     *
     * @param expense El objeto {@link Expense} a eliminar. Este debe tener una categoria asignada
     *                que coincida con una existente en el mapa interno.
     * @return {@code true} si el gasto se elimina correctamente; {@code false} si el gasto no se
     * encuentra asociado a la categoria indicada o no se pueden realizar cambios.
     * @throws NullPointerException si el parametro {@code expense} es null.
     *                              </body>
     */
    public final boolean removeExpenseFromExpenseMap(Expense expense) {
        if (expense == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro de expense no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro de " +
                                                   "expense no puede ser nulo");
        }

        if (this.applicationDataPojoExpensesMap == null) {
            Log.e("Fatal Error Code 0x0000 [Raised]", "Se ha intentado acceder a un parametro nulo" +
                    " en un singleton, error de estado");
            return false;
        }

        List<Expense> categoryExpenses =
                this.applicationDataPojoExpensesMap.get(expense.getExpenseCategory());
        if (categoryExpenses == null) {
            return false;
        }
        return categoryExpenses.remove(expense);
    }


    /**
     * <body style="color:white;">
     * Obtiene todos los gastos asociados a una categoria especifica.
     *
     * <p>
     * Este metodo utiliza la estructura interna {@link Map} que relaciona nombres de categorias de
     * gastos con sus respectivas listas de transacciones {@link Expense}. Si la categoria
     * proporcionada no existe en el mapa, el metodo devuelve una lista vacia.
     * </p>
     *
     * @param expenseCategory el objeto {@link ExpenseCategory} que representa la categoria cuyos
     *                        gastos asociados se desean obtener.
     * @return una lista de objetos {@link Expense} asociados a la categoria proporcionada. Si no
     * hay transacciones asociadas, devuelve una lista vacia.
     * @throws NullPointerException si el parametro {@code expenseCategory} es nulo.
     *                              </body>
     */
    public final List<Expense> getAllExpensesPerCategory(ExpenseCategory expenseCategory) {
        return this.applicationDataPojoExpensesMap.getOrDefault(
                expenseCategory.getExpenseCategoryName(), new ArrayList<>());
    }

    /**
     * <body style="color:white;">
     * Elimina todos los gastos asociados a una categoria especifica.
     *
     * <p>
     * Este metodo elimina del mapa interno {@link Map} la entrada que corresponde a la categoria
     * proporcionada. Retorna la lista de gastos que estaba asociada a la categoria antes de ser
     * eliminada. Si la categoria no existe en el mapa, devuelve {@code null}.
     * </p>
     *
     * @param expenseCategory el objeto {@link ExpenseCategory} que representa la categoria cuyos
     *                        gastos asociados se desean eliminar.
     * @return una lista de objetos {@link Expense} que representa los gastos eliminados. Si la
     * categoria no existe en el mapa, devuelve {@code null}.
     * @throws NullPointerException si el parametro {@code expenseCategory} es nulo.
     *                              </body>
     */
    public final List<Expense> dropAllExpensesPerCategory(ExpenseCategory expenseCategory) {
        return this.applicationDataPojoExpensesMap.remove(
                expenseCategory.getExpenseCategoryName());
    }

    /**
     * <body style="color:white;">
     * Calcula el total acumulado de los gastos asociados a una categoria especifica.
     *
     * <p>
     * Este metodo toma una instancia de {@link ExpenseCategory} como entrada y obtiene la lista de
     * gastos asociados a esa categoria utilizando el metodo
     * {@link #getAllExpensesPerCategory(ExpenseCategory)}. Posteriormente, usa la funcionalidad de
     * <i>streams</i> de Java para procesar y reducir la lista de gastos, calculando la suma de los
     * valores registrados en cada objeto {@link Expense}.
     * </p>
     * @param expenseCategory el objeto {@link ExpenseCategory} que representa la categoria cuyo
     *                        total acumulado de gastos se desea calcular.
     * @return un objeto {@link BigDecimal} que representa el total acumulado de los valores de
     * todos los gastos asociados a la categoria proporcionada. Si la categoria no tiene gastos, el
     * resultado sera {@link BigDecimal#ZERO}.
     * @throws NullPointerException si el parametro {@code expenseCategory} es nulo.
     *                              </body>
     */
    public final BigDecimal getTotalPerCategory(ExpenseCategory expenseCategory) {
        List<Expense> associatedExpenseListing = getAllExpensesPerCategory(expenseCategory);

        return associatedExpenseListing
                .stream()
                .map(Expense::getExpenseValueRegistered)
                .reduce(BigDecimal.ZERO, new BinaryOperator<BigDecimal>() {
                    @Override
                    public BigDecimal apply(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                        return bigDecimal.add(bigDecimal2);
                    }
                });
    }

}
