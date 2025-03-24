package com.evolvlabs.trackthatexpense.PersistencyEngine;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo implementa una clase serializable representativa de una
 * categoria de gastos para nuestro sistema, esta clase es util ya que nos permite organizar
 */
public class ExpenseCategory implements Comparable<ExpenseCategory>, Serializable {

    /*! Parametros Internos*/
    private String expenseCategoryName;
    private BigDecimal expenseCategoryMaxPerExpenseValue;
    private BigDecimal expenseCategoryMaxPerMonthValue;

    /*! Constructors*/


    /**
     * Constructor que inicializa una nueva instancia de la clase {@code ExpenseCategory}. Este
     * constructor permite definir el nombre de la categoria de gastos, su valor maximo permitido
     * por cada gasto y su valor maximo permitido por mes.
     *
     * @param expenseCategoryName               nombre de la categoria de gasto. No debe ser vacio.
     *                                          El metodo valida que este parametro sea valido a
     *                                          traves del metodo {@code setExpenseCategoryName()}.
     * @param expenseCategoryMaxPerExpenseValue valor maximo permitido por cada gasto en esta
     *                                          categoria. No puede ser nulo ni menor que 0. Estos
     *                                          valores son validados a traves del metodo
     *                                          {@code setExpenseCategoryMaxPerExpenseValue()}.
     * @param expenseCategoryMaxPerMonthValue   valor maximo permitido para esta categoria por mes.
     *                                          No puede ser nulo ni menor que 0. Este valor es
     *                                          validado a traves del metodo
     *                                          {@code setExpenseCategoryMaxPerMonthValue()}.
     * @throws IllegalArgumentException si {@code expenseCategoryName} esta vacio o si alguno de los
     *                                  valores numericos ingresados es menor a 0.
     * @throws NullPointerException     si alguno de los valores numericos es nulo.
     */
    public ExpenseCategory(String expenseCategoryName,
                           BigDecimal expenseCategoryMaxPerExpenseValue,
                           BigDecimal expenseCategoryMaxPerMonthValue) {
        this.setExpenseCategoryName(expenseCategoryName);
        this.setExpenseCategoryMaxPerExpenseValue(expenseCategoryMaxPerExpenseValue);
        this.setExpenseCategoryMaxPerMonthValue(expenseCategoryMaxPerMonthValue);
    }

    /**
     * <body style="color:white;">
     * Este constructor permite inicializar una nueva instancia de la clase {@code ExpenseCategory}
     * estableciendo el nombre de la categoria de gasto y el valor maximo permitido por mes. El
     * metodo valida que los parametros ingresados sean validos utilizando los setters apropiados.
     * Si alguno de los valores ingresados no cumple con los requisitos, el metodo lanza una
     * excepcion.
     * </body>
     *
     * @param expenseCategoryName             Nombre de la categoria de gasto. No debe ser vacio.
     *                                        Validado a traves de
     *                                        {@code setExpenseCategoryName()}.
     * @param expenseCategoryMaxPerMonthValue Valor maximo permitido para esta categoria por mes. No
     *                                        puede ser nulo ni menor que 0. Validado a traves de
     *                                        {@code setExpenseCategoryMaxPerMonthValue()}.
     * @throws IllegalArgumentException Si {@code expenseCategoryName} esta vacio o si
     *                                  {@code expenseCategoryMaxPerMonthValue} es menor que 0.
     * @throws NullPointerException     Si {@code expenseCategoryMaxPerMonthValue} es nulo.
     */
    public ExpenseCategory(String expenseCategoryName, BigDecimal expenseCategoryMaxPerMonthValue) {
        this.setExpenseCategoryName(expenseCategoryName);
        this.setExpenseCategoryMaxPerMonthValue(expenseCategoryMaxPerMonthValue);
    }

    /**
     * <body style="color:white;">
     * Constructor por defecto de la clase {@code ExpenseCategory}. Este constructor inicializa una
     * instancia de la clase configurando valores predefinidos para sus atributos internos. El
     * objetivo de este constructor por defecto es facilitar la creacion de instancias de la clase
     * para escenarios donde no se requiere especificar valores.
     * <ul>
     * <li>El atributo {@code expenseCategoryName} es inicializado con el valor predefinido "Default".</li>
     * <li>El atributo {@code expenseCategoryMaxPerExpenseValue} es inicializado con {@code BigDecimal.ZERO}.</li>
     * <li>El atributo {@code expenseCategoryMaxPerMonthValue} es inicializado con {@code BigDecimal.ZERO}.</li>
     * </ul>
     * En este constructor no se realiza ninguna validacion adicional, ya que los valores
     * iniciales garantizan consistencia.
     * </body>
     */
    public ExpenseCategory() {
        this.setExpenseCategoryName("Default");
        this.setExpenseCategoryMaxPerExpenseValue(BigDecimal.ZERO);
        this.setExpenseCategoryMaxPerMonthValue(BigDecimal.ZERO);
    }

    /*! Getters y Setters internos*/

    /**
     * <body style="color:white;">
     * Este metodo establece el valor de {@code expenseCategoryName}, asegurandose de que sea un
     * valor valido. Si el parametro proporcionado no cumple con los requisitos establecidos (por
     * ejemplo, es una cadena vacia), se lanza una excepcion para indicar dicho error. Este metodo
     * utiliza validacion directa empleando el metodo {@code isEmpty()}.
     *
     * @param expenseCategoryName El nombre de la categoria de gasto. No debe ser vacio. Validado a
     *                            traves de una comprobacion {@code String.isEmpty()}.
     * @throws IllegalArgumentException Si el parametro proporcionado es vacio.
     * @see IllegalArgumentException
     * </body>
     */
    public void setExpenseCategoryName(String expenseCategoryName)
            throws IllegalArgumentException {
        if (expenseCategoryName.isEmpty()) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "set_expenseCategoryName es invalido, no puede estar vacio");
            throw new IllegalArgumentException("Error Code 0x0001 - [Raised] El parametro " +
                                                       "ingresado para el metodo " +
                                                       "set_expeseCategoryName es invalido, no " +
                                                       "puede estar vacio");
        } else {
            this.expenseCategoryName = expenseCategoryName;
        }
    }

    /**
     * <body style="color:white;">
     * Este metodo proporciona el nombre de la categoria de gastos tal como fue configurado
     * previamente para esta instancia de {@code ExpenseCategory}. Este valor representa un
     * identificador unico y no vacio que se utiliza para distinguir esta categoria de otras.
     *
     * <p>
     * El metodo no toma ningun parametro de entrada y simplemente devuelve el valor almacenado
     * internamente en la variable {@code expenseCategoryName}. Dado que este atributo es privado,
     * este metodo actua como un punto de acceso controlado para su recuperacion.
     * </p>
     *
     * <ul>
     *     <li>El atributo {@code expenseCategoryName} debe estar configurado antes de llamar a
     *     este metodo para evitar informacion incorrecta.</li>
     *     <li>No realiza validaciones ni operaciones adicionales, solo retorna el valor actual
     *     almacenado internamente.</li>
     * </ul>
     *
     * @return Una cadena no nula y no vacia que representa el nombre de la categoria de gasto
     * asociado a esta instancia.
     * </body>
     */
    public String getExpenseCategoryName() {
        return expenseCategoryName;
    }

    /**
     * <body style="color:white;">
     * Este metodo establece el valor maximo permitido por cada gasto individual en esta categoria.
     * Para garantizar la validez de los datos, el metodo verifica que el parametro no sea nulo y
     * que tenga un valor mayor o igual a cero.
     *
     * <p>Si el parametro {@code expenseCategoryMaxPerExpenseValue} no cumple con los requisitos
     * establecidos, el metodo lanza una excepcion para indicar un error en la operacion:
     * <ul>
     *     <li>{@code NullPointerException}: Si el parametro es nulo.</li>
     *     <li>{@code IllegalArgumentException}: Si el valor proporcionado es menor a cero.</li>
     * </ul>
     *
     * <p>Este metodo fomenta el uso de {@link BigDecimal} para asegurar una gran precision
     * al manejar valores monetarios o decimales extensos, lo que es adecuado para operaciones
     * financieras.
     * </body>
     *
     * @param expenseCategoryMaxPerExpenseValue El limite maximo permitido por gasto individual para
     *                                          esta categoria. No puede ser nulo ni menor que
     *                                          cero.
     * @throws IllegalArgumentException Si el valor es menor que cero.
     * @throws NullPointerException     Si el parametro es nulo.
     */
    public void setExpenseCategoryMaxPerExpenseValue(BigDecimal expenseCategoryMaxPerExpenseValue)
            throws IllegalArgumentException, NullPointerException {
        if (expenseCategoryMaxPerExpenseValue == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "set_expenseCategoryMaxPerExpenseValue es invalido, no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro ingresado " +
                                                   "para el metodo set_expenseCategoryMaxPerExpenseValue es invalido, no puede " +
                                                   "ser nulo");
        }

        if (expenseCategoryMaxPerExpenseValue.compareTo(BigDecimal.ZERO) < 0) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "set_expenseCategoryMaxPerExpenseValue es invalido, no puede ser menor a 0");
            throw new IllegalArgumentException("Error Code 0x0001 - [Raised] El parametro " +
                                                       "ingresado para el metodo set_expenseCategoryMaxPerExpenseValue es invalido, " +
                                                       "no puede ser menor a 0");
        }

        this.expenseCategoryMaxPerExpenseValue = expenseCategoryMaxPerExpenseValue;
    }

    /**
     * <body style="color:white;">
     * Este metodo obtiene el valor maximo permitido por gasto individual para esta categoria de
     * gastos. El valor devuelto representa un limite superior establecido para cada gasto en esta
     * categoria. Dado que el metodo retorna un objeto de tipo {@link BigDecimal}, asegura una
     * representacion precisa para valores decimales y operaciones aritmeticas.
     * <ul>
     *     <li>Representa un limite por transaccion.</li>
     *     <li>Es un valor definido mediante {@code setExpenseCategoryMaxPerExpenseValue()}.</li>
     * </ul>
     * Este metodo es util para realizar validaciones o establecer restricciones
     * personalizadas en los valores de gastos individuales.
     * </body>
     *
     * @return Un objeto {@link BigDecimal} que representa el valor maximo permitido por gasto
     * individual para esta categoria.
     * </body>
     */
    public BigDecimal getExpenseCategoryMaxPerExpenseValue() {
        return expenseCategoryMaxPerExpenseValue;
    }


    /**
     * <body style="color:white;">
     * Este metodo establece el valor maximo permitido para los gastos mensuales de esta categoria.
     * El metodo asegura que el parametro proporcionado sea valido, verificando que no sea nulo y
     * que su valor no sea menor a cero.
     *
     * <p>Si {@code expenseCategoryMaxPerMonthValue} no cumple con los requisitos, este metodo
     * lanza
     * una excepcion para indicar el error encontrado:
     * <ul>
     *     <li>{@link NullPointerException}: Si el parametro proporcionado es nulo.</li>
     *     <li>{@link IllegalArgumentException}: Si el parametro proporcionado es menor a cero.</li>
     * </ul>
     *
     * <p>Este metodo utiliza {@link BigDecimal}, que ofrece una representacion precisa para valores
     * decimales, siendo especialmente adecuado para operaciones financieras como el manejo de
     * valores monetarios. Las validaciones incluidas permiten garantizar la integridad de los datos
     * asociados a esta categoria de gastos.
     * </body>
     *
     * @param expenseCategoryMaxPerMonthValue El valor maximo permitido para los gastos mensuales de
     *                                        esta categoria. No puede ser nulo ni menor a cero.
     * @throws NullPointerException     Si el parametro es nulo.
     * @throws IllegalArgumentException Si el parametro es menor que cero.
     */
    public void setExpenseCategoryMaxPerMonthValue(BigDecimal expenseCategoryMaxPerMonthValue)
            throws NullPointerException, IllegalArgumentException {

        if (expenseCategoryMaxPerMonthValue == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "set_expenseCategoryMaxPerMonthValue es invalido, no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro ingresado " +
                                                   "para el metodo set_expenseCategoryMaxPerMonthValue es invalido, no puede " +
                                                   "ser nulo");
        }

        if (expenseCategoryMaxPerMonthValue.compareTo(BigDecimal.ZERO) < 0) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "set_expenseCategoryMaxPerMonthValue es invalido, no puede ser menor a 0");
            throw new IllegalArgumentException("Error Code 0x0001 - [Raised] El parametro " +
                                                       "ingresado para el metodo set_expenseCategoryMaxPerMonthValue es invalido, " +
                                                       "no puede ser menor a 0");
        }

        this.expenseCategoryMaxPerMonthValue = expenseCategoryMaxPerMonthValue;
    }

    /**
     * <body style="color:white;">
     * Este metodo obtiene el valor maximo permitido para los gastos mensuales de esta categoria de
     * gastos. El metodo utiliza el atributo {@code expenseCategoryMaxPerMonthValue} para devolver
     * un valor que representa el limite superior monetario mensual configurado para esta instancia
     * de {@code ExpenseCategory}.
     *
     * <ul>
     * <li>El valor retornado es obtenido directamente del campo interno
     * {@code expenseCategoryMaxPerMonthValue}, el cual debe haber sido configurado previamente
     * mediante el metodo {@code setExpenseCategoryMaxPerMonthValue()}.</li>
     * <li>El metodo no realiza ningun tipo de validacion o procesamiento adicional, ya que
     * asume que el valor almacenado es valido.</li>
     * </ul>
     *
     * <p>El uso de {@link BigDecimal} asegura una precision optima al trabajar con valores
     * monetarios y permite evitar errores de redondeo comunes en representaciones menos precisas.
     *
     * @return Un objeto {@link BigDecimal} que representa el limite maximo permitido para los
     * gastos mensuales de esta categoria.
     * </body>
     */
    public BigDecimal getExpenseCategoryMaxPerMonthValue() {
        return expenseCategoryMaxPerMonthValue;
    }


    /*Sobrecargas necesarias*/

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())){
            return false;
        }
        else if (obj == this){
            return true;
        }
        else {
            ExpenseCategory categoryConverted = (ExpenseCategory) obj;
            return (this.getExpenseCategoryName()
                    .equals(categoryConverted.getExpenseCategoryName())
            && this.getExpenseCategoryMaxPerMonthValue()
                    .equals(categoryConverted.getExpenseCategoryMaxPerMonthValue())
            && this.getExpenseCategoryMaxPerExpenseValue()
                    .equals(categoryConverted.getExpenseCategoryMaxPerExpenseValue()));
        }
    }

    /**
     * Returns a string representation of the object. In general, the {@code toString} method
     * returns a string that "textually represents" this object. The result should be a concise but
     * informative representation that is easy for a person to read. It is recommended that all
     * subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object} returns a string consisting of the name
     * of the class of which the object is an instance, the at-sign character `{@code @}', and the
     * unsigned hexadecimal representation of the hash code of the object. In other words, this
     * method returns a string equal to the value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public @NotNull String toString() {
        return this.getExpenseCategoryName();
    }

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff {@code y.compareTo(x)} throws
     * an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any class that implements
     * the {@code Comparable} interface and violates this condition should clearly indicate this
     * fact.  The recommended language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it from being compared
     *                              to this object.
     */
    @Override
    public int compareTo(ExpenseCategory o) {
        if (o == null) {
            Log.e("Error Code 0x0001 - [Raised]", "El parametro ingresado para el metodo " +
                    "compareTo es invalido, no puede ser nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] El parametro ingresado " +
                                                   "para el metodo compareTo es invalido, no puede ser nulo");
        }

        if (this.expenseCategoryMaxPerMonthValue == null || o.expenseCategoryMaxPerMonthValue == null) {
            Log.e("Error Code 0x0001 - [Raised]", "Uno de los valores de maxPerMonthValue es nulo");
            throw new NullPointerException("Error Code 0x0001 - [Raised] Uno de los valores de " +
                                                   "maxPerMonthValue es nulo");
        }

        return this.expenseCategoryMaxPerMonthValue.compareTo(o.expenseCategoryMaxPerMonthValue);


    }
}
