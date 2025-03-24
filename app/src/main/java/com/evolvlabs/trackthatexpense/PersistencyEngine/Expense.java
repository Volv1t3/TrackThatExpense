package com.evolvlabs.trackthatexpense.PersistencyEngine;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo incluye la clase base del modelo de persistencia para
 * nuestra aplicacion. La idea de esta clase es implementar una clase Serializable que nos
 * permita convertir un POJO Expense, para que este sea serializable y guardable en la
 * implementacion de la SharedPreferences de la aplicacion.
 */
public class Expense implements Comparable<Expense>, Serializable {

    /*! Parametros internos*/
    @SerializedName("expenseCategoryName")
    private String expenseCategory;

    @SerializedName("expenseDateRegistered")
    private Date expenseDateRegistered;

    @SerializedName("expenseValueRegistered")
    private BigDecimal expenseValueRegistered;

    /*! Constructores*/


    /**
     * <body style="color:white;">
     * Constructor para inicializar un objeto de tipo Expense. Este constructor permite la creacion
     * de una instancia utilizando parametros especificos de categoria del gasto, fecha en que se
     * registro el gasto y valor del gasto.
     *
     * @param expenseCategory        La categoria del gasto a registrar. No puede ser nulo, de lo
     *                               contrario lanzara una excepcion {@link NullPointerException}.
     * @param expenseDateRegistered  La fecha en la que se registro el gasto. No puede ser nulo ni
     *                               posterior a la fecha actual, de lo contrario lanzara una
     *                               excepcion {@link NullPointerException} o
     *                               {@link IllegalArgumentException}.
     * @param expenseValueRegistered El valor registrado del gasto. No puede ser nulo ni negativo,
     *                               de lo contrario lanzara una excepcion
     *                               {@link NullPointerException} o
     *                               {@link IllegalArgumentException}.
     * @throws NullPointerException     Si alguno de los parametros es nulo.
     * @throws IllegalArgumentException Si la fecha es posterior a la actual o si el valor del gasto
     *                                  es negativo.
     *                                  </body>
     */
    public Expense(String expenseCategory, Date expenseDateRegistered,
                   BigDecimal expenseValueRegistered) {
        this.setExpenseCategory(expenseCategory);
        this.setExpenseDateRegistered(expenseDateRegistered);
        this.setExpenseValueRegistered(expenseValueRegistered);
    }

    /**
     * <body style="color:white;">
     * Constructor para inicializar un objeto de tipo Expense. Este constructor facilita la creacion
     * de una instancia generica, donde la categoria del gasto debe ser especifica, y el valor del
     * gasto esta asociado a la fecha de creacion, asignada automaticamente como la fecha actual del
     * sistema.
     *
     * <p>El metodo utiliza los setters internos de la clase para verificar la validez de los
     * parametros proporcionados. Esto incluye validar que:</p>
     * <ul>
     *     <li><b>expenseCategory:</b> No puede ser nulo; de lo contrario, lanzara una
     *     excepcion {@link NullPointerException}.</li>
     *     <li><b>expenseValueRegistered:</b> No puede ser nulo ni negativo; de lo contrario,
     *     lanzara una excepcion {@link NullPointerException} o
     *     {@link IllegalArgumentException}.</li>
     * </ul>
     *
     * @param expenseCategory        La categoria del gasto que sera registrada. No puede ser nula
     *                               ya que esto causa una excepcion {@link NullPointerException}.
     * @param expenseValueRegistered El valor del gasto correspondiente. No debe ser nulo ni
     *                               negativo, ya que esto causara una excepcion
     *                               {@link NullPointerException} o
     *                               {@link IllegalArgumentException}.
     * @throws NullPointerException     Si alguno de los parametros es nulo.
     * @throws IllegalArgumentException Si el valor del gasto es negativo.
     *                                  </body>
     */
    public Expense(String expenseCategory, BigDecimal expenseValueRegistered) {
        this.setExpenseCategory(expenseCategory);
        this.setExpenseDateRegistered(Date.from(Instant.now()));
        this.setExpenseValueRegistered(expenseValueRegistered);
    }

    /**
     * <body style="color:white;">
     * Constructor sin argumentos de la clase {@code Expense}. Este constructor inicializa una
     * instancia de tipo {@code Expense} utilizando valores predeterminados:
     *
     * <ul>
     *     <li><b>Categoria del gasto:</b> Se inicializa como "Default".</li>
     *     <li><b>Fecha de registro:</b> Se asigna automaticamente como la fecha y hora actuales del sistema.</li>
     *     <li><b>Valor del gasto:</b> Se asigna como 0.00 utilizando una instancia de {@link BigDecimal}.</li>
     * </ul>
     *
     * <p>La idea de este constructor es proporcionar una manera rapida de crear un objeto {@code Expense}
     * con valores seguros y predeterminados, permitiendo evitar posibles excepciones relacionadas con parametros nulos o valores invalidos.
     * Internamente, este constructor utiliza los setters de la clase para asegurarse de que las validaciones basicas se apliquen
     * correctamente antes de inicializar el objeto.</p>
     *
     * @throws NullPointerException Este constructor no lanza esta excepcion directamente, pero las
     *                              validaciones de los setters pueden hacerlo en caso de fallos
     *                              inesperados durante la inicializacion.
     *                              </body>
     */
    public Expense() {
        this.setExpenseCategory("Default");
        this.setExpenseDateRegistered(Date.from(Instant.now()));
        this.setExpenseValueRegistered(BigDecimal.ZERO);
    }

    /*! Getters y Setters*/

    /**
     * <body style="color:white;">
     * Metodo para establecer la categoria del gasto asociado al objeto {@code Expense}.
     * <p>
     * Este metodo utiliza un sistema de validacion interna para garantizar que el parametro
     * proporcionado no sea nulo. En caso de que el parametro {@code expenseCategory} sea nulo se
     * lanza una excepcion {@link NullPointerException}, y ademas se registra un mensaje de error
     * utilizando {@link Log}.
     * </p>
     * <p>
     * Al asignar un nuevo valor a la categoria del gasto, este es almacenado en la propiedad
     * interna {@code expenseCategory}.
     * </p>
     *
     * @param expenseCategory Categoria del gasto a registrar en el objeto. No puede ser nulo, de lo
     *                        contrario se lanzara una excepcion.
     * @throws NullPointerException Si el parametro {@code expenseCategory} es {@code null}.
     *                              </body>
     */
    public void setExpenseCategory(String expenseCategory) throws NullPointerException {

        if (expenseCategory == null) {
            Log.e("Error Code 0x001 [Raised]", "Error Code 0x001 - [Raised] El parametro de " +
                    "expenseCategory no " +
                    "puede ser nulo");
            throw new NullPointerException("Error Code 0x001 - [Raised] El parametro de " +
                                                   "expenseCategory no " +
                                                   "puede ser nulo");
        }

        this.expenseCategory = expenseCategory;
    }

    /**
     * <body style="color:white;">
     * Metodo para obtener la categoria de gasto registrada en este objeto {@code Expense}.
     *
     * <p>Este metodo devuelve el valor de la propiedad {@code expenseCategory}, representando
     * la categoria asociada al gasto. La propiedad es almacenada como una cadena de texto y no
     * puede ser nula.</p>
     *
     * @return el valor actual de la categoria de gasto registrada como un objeto {@link String}. Si
     * el objeto ha sido correctamente inicializado, el valor nunca sera {@code null}.
     * @see #setExpenseCategory(String)
     * </body>
     */
    public String getExpenseCategory() {
        return expenseCategory;
    }

    /**
     * <body style="color:white;">
     * Metodo para establecer la fecha en la que se registro el gasto en el objeto {@code Expense}.
     * <p>
     * Este metodo utiliza un sistema de validacion interna para verificar que el parametro
     * proporcionado es valido segun las siguientes condiciones:
     * <ul>
     *     <li>No puede ser {@code null}. Si el valor del parametro es {@code null}, se lanza una
     *     excepcion {@link NullPointerException} y se registra un mensaje de error utilizando
     *     {@link Log}.</li>
     *     <li>No puede ser una fecha posterior a la fecha actual del sistema. Si se detecta que el
     *     parametro {@code expenseDateRegistered} es una fecha posterior, el metodo lanza una excepcion
     *     {@link IllegalArgumentException} y lo registra mediante {@link Log}.</li>
     * </ul>
     * <p>
     * El objetivo de este metodo es garantizar la integridad de los datos del objeto {@code Expense}
     * al validar la fecha antes de asignarla.
     *
     * @param expenseDateRegistered La fecha en la que se registro el gasto. Debe ser una instancia
     *                              valida de {@link Date} que no sea {@code null} y no represente
     *                              una fecha futura.
     * @throws NullPointerException     Si el parametro {@code expenseDateRegistered} es
     *                                  {@code null}.
     * @throws IllegalArgumentException Si el parametro {@code expenseDateRegistered} representa una
     *                                  fecha posterior a la fecha actual del sistema.
     *                                  </body>
     */
    public void setExpenseDateRegistered(Date expenseDateRegistered) throws NullPointerException
            , IllegalArgumentException {
        if (expenseDateRegistered == null) {
            Log.e("Error Code 0x001 [Raised]", "Error Code 0x001 - [Raised] El parametro de " +
                    "expenseDateRegistered no " +
                    "puede ser nulo");
            throw new NullPointerException("Error Code 0x001 - [Raised] El parametro de " +
                                                   "expenseDateRegistered no " +
                                                   "puede ser nulo");
        }

        if (expenseDateRegistered.after(Date.from(Instant.now()))) {
            Log.e("Error Code 0x001 [Raised]", "Error Code 0x001 - [Raised] El parametro de " +
                    "expenseDateRegistered no " +
                    "puede ser una fecha posterior a la actual");
            throw new IllegalArgumentException("Error Code 0x001 - [Raised] El parametro de " +
                                                       "expenseDateRegistered no " +
                                                       "puede ser una fecha posterior a la actual");
        }

        this.expenseDateRegistered = expenseDateRegistered;
    }

    /**
     * <body style="color:white;">
     * Metodo para obtener la fecha registrada del gasto en este objeto {@code Expense}.
     *
     * <p>Este metodo devuelve el valor de la propiedad interna {@code expenseDateRegistered},
     * que representa la fecha en la cual se registro el gasto. La fecha es almacenada como un
     * objeto {@link Date} que es generado e inicializado de manera segura utilizando validaciones
     * integradas en esta clase.</p>
     *
     * <p>El metodo no toma parametros ni lanza excepciones porque solo devuelve el valor
     * almacenado
     * en la propiedad interna {@code expenseDateRegistered}, que fue validado al momento de su
     * asignacion con el {@link #setExpenseDateRegistered(Date)}.</p>
     *
     * @return una instancia de la clase {@link Date}, representando la fecha valida y segura en la
     * cual se registro el gasto.
     * @see #setExpenseDateRegistered(Date)
     * </body>
     */
    public Date getExpenseDateRegistered() {
        return expenseDateRegistered;
    }

    public void setExpenseValueRegistered(BigDecimal expenseValueRegistered) throws NullPointerException,
            IllegalArgumentException{

        if (expenseValueRegistered == null){
            Log.e("Error Code 0x001 [Raised]","Error Code 0x001 - [Raised] El parametro de " +
                    "expenseValueRegistered no " +
                    "puede ser nulo");
            throw new NullPointerException("Error Code 0x001 - [Raised] El parametro de " +
                    "expenseValueRegistered no " +
                    "puede ser nulo");
        }

        if (expenseValueRegistered.compareTo(BigDecimal.ZERO) < 0){
            Log.e("Error Code 0x001 [Raised]","Error Code 0x001 - [Raised] El parametro de " +
                    "expenseValueRegistered no " +
                    "puede ser negativo");
            throw new IllegalArgumentException("Error Code 0x001 - [Raised] El parametro de " +
                    "expenseValueRegistered no " +
                    "puede ser negativo");
        }

        this.expenseValueRegistered = expenseValueRegistered;
    }

    /**
     * <body style="color:white;">
     * Metodo para obtener el valor monetario registrado en el objeto {@code Expense}.
     *
     * <p>Este metodo devuelve el valor de la propiedad interna {@code expenseValueRegistered},
     * que representa el valor registrado del gasto. La propiedad es almacenada como una instancia
     * {@link BigDecimal}, garantizando precision en las operaciones aritmeticas financieras.</p>
     *
     * <p>El metodo no lanza excepciones ya que el valor {@code expenseValueRegistered} se valida
     * en el momento de su asignacion utilizando el metodo
     * {@link #setExpenseValueRegistered(BigDecimal)}. Esto incluye las siguientes
     * validaciones:</p>
     * <ul>
     *  <li>No puede ser {@code null}; una violacion lanza {@link NullPointerException}
     *  durante la configuracion.</li>
     *  <li>No puede ser menor que 0 (negativo); una violacion lanza {@link IllegalArgumentException}
     *  en el momento de su configuracion.</li>
     * </ul>
     *
     * @return una instancia {@link BigDecimal} representando el valor monetario registrado del
     * gasto. El valor retornado ha sido validado previamente.
     * @see #setExpenseValueRegistered(BigDecimal)
     * </body>
     */
    public BigDecimal getExpenseValueRegistered() {
        return expenseValueRegistered;
    }

    /*! Overloads y Comparable*/

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
        } else if (obj == this){
            return true;
        } else {
            Expense transpiledExpense = (Expense) obj;
            return (this.getExpenseCategory().equals(transpiledExpense.getExpenseCategory())
                    &&
                    this.getExpenseDateRegistered().equals(transpiledExpense.getExpenseDateRegistered())
                    &&
                    this.getExpenseValueRegistered().equals(transpiledExpense.getExpenseValueRegistered())
            );
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
        return "Expense{" +
                "_expenseCategory=" + expenseCategory +
                ", _expenseDateRegistered=" + expenseDateRegistered +
                ", _expenseValueRegistered=" + expenseValueRegistered +
                '}';
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
    public int compareTo(Expense o) {
        if (o == null){
            Log.e("Error Code 0x001 [Raised]","Error Code 0x001 - [Raised] El parametro de " +
                    "expenseCategory no " +
                    "puede ser nulo");
            throw new NullPointerException("Error Code 0x001 - [Raised] El parametro de " +
                    "expenseCategory no " +
                    "puede ser nulo");
        } else {
            return this.getExpenseValueRegistered()
                    .compareTo(o.getExpenseValueRegistered());
        }
    }
}
