package com.evolvlabs.trackthatexpense.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ApplicationDataPOJO;
import com.evolvlabs.trackthatexpense.PersistencyEngine.Expense;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ExpenseCategory;
import com.evolvlabs.trackthatexpense.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author : Paulo Cantos, Santiago Arellano
 * @date : 21st-Mar-2025
 * @description: El presente archivo incluye los metodos basicos para serializar y deserializar
 * desde el archivo del SharedPreferences que contiene una estructura JSON para manejar los
 * archivos serializados y creados por el usuario. La idea es que el programa al ejecutarse
 * escribe diferente informacion al SharedPreferences, permitiendo la carga y descarga de la
 * informacion dependiendo del tipo de informacion a serializar, por ejemplo al cerrar la
 * aplicacion guardamos toda la informacion contenida en las clases Singleton para el manejo de
 * categorias e inclusive expenses.
 */
public class SerializationEngine {




    private final Gson _gsonInstanceForSerialization = new GsonBuilder()
            .registerTypeAdapter(BigDecimal.class, new TypeAdapter<BigDecimal>() {
                /**
                 * Writes one JSON value (an array, object, string, number, boolean or null)
                 * for {@code value}.
                 *
                 * @param out
                 * @param value the Java object to write. May be null.
                 */
                @Override
                public void write(JsonWriter out, BigDecimal value) throws IOException {
                    out.value(value.toString());
                }

                /**
                 * Reads one JSON value (an array, object, string, number, boolean or null)
                 * and converts it to a Java object. Returns the converted object.
                 *
                 * @param in
                 * @return the converted Java object. May be null.
                 */
                @Override
                public BigDecimal read(JsonReader in) throws IOException {
                    return new BigDecimal(in.nextString());
                }
            })
            .registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
                /**
                 * Writes one JSON value (an array, object, string, number, boolean or null)
                 * for {@code value}.
                 *
                 * @param out
                 * @param value the Java object to write. May be null.
                 */
                @Override
                public void write(JsonWriter out, Date value) throws IOException {
                    out.value(value.getTime());
                }

                /**
                 * Reads one JSON value (an array, object, string, number, boolean or null)
                 * and converts it to a Java object. Returns the converted object.
                 *
                 * @param in
                 * @return the converted Java object. May be null.
                 */
                @Override
                public Date read(JsonReader in) throws IOException {
                    return new Date(in.nextLong());
                }
            })
            .registerTypeAdapter(new TypeToken<Map<String, List<Expense>>>() {
                                   }.getType(),
                                   new TypeAdapter<HashMap<String, List<Expense>>>() {
                                       /**
                                        * Writes one JSON value (an array, object, string, number, boolean
                                        * or null) for {@code value}.
                                        *
                                        * @param out
                                        * @param value the Java object to write. May be null.
                                        */
                                       @Override
                                       public void write(JsonWriter out,
                                                         HashMap<String, List<Expense>> value) throws IOException {
                                           if (value == null) {
                                               out.nullValue();
                                               return;
                                           }
                                           out.beginObject();
                                           for (Map.Entry<String, List<Expense>> entryFromMap :
                                                   value.entrySet()) {
                                               out.name(entryFromMap.getKey());
                                               if (entryFromMap.getValue() == null) {
                                                   out.nullValue();
                                               } else {
                                                   out.beginArray();
                                                   for (Expense registeredExpense :
                                                           entryFromMap.getValue()) {
                                                       out.beginObject();
                                                       out.name("expenseCategoryName");
                                                       out.value(registeredExpense
                                                                         .getExpenseCategory());

                                                       out.name("expenseRegisteredDate");
                                                       out.value(registeredExpense
                                                                         .getExpenseDateRegistered()
                                                                         .getTime());

                                                       out.name("expenseValueRegistered");
                                                       out.value(registeredExpense
                                                                         .getExpenseValueRegistered()
                                                                         .toString());
                                                       out.endObject();
                                                   }
                                                   out.endArray();
                                               }
                                           }
                                           out.endObject();
                                       }

                                       /**
                                        * Reads one JSON value (an array, object, string, number, boolean
                                        * or null) and converts it to a Java object. Returns the converted
                                        * object.
                                        *
                                        * @param in
                                        * @return the converted Java object. May be null.
                                        */
                                       @Override
                                       public HashMap<String, List<Expense>> read(JsonReader in) throws IOException {
                                           if (in.peek() == JsonToken.NULL) {
                                               in.nextNull();
                                               return null;
                                           }

                                           HashMap<String, List<Expense>> deserializedMap =
                                                   new HashMap<>();
                                           in.beginObject();
                                           while (in.hasNext()) {
                                               String registeredCategory = in.nextName();
                                               List<Expense> expenseList = new ArrayList<>();
                                               in.beginArray();
                                               while (in.hasNext()) {
                                                   String expenseRegisteredCategory = null;
                                                   Date expenseRegisteredDate = null;
                                                   BigDecimal expenseValueRegistered = null;

                                                   in.beginObject();
                                                   while (in.hasNext()) {
                                                       String expenseFieldName = in.nextName();
                                                       switch (expenseFieldName) {
                                                           case "expenseCategoryName":
                                                               expenseRegisteredCategory =
                                                                       in.nextString();
                                                               break;
                                                           case "expenseRegisteredDate":
                                                               expenseRegisteredDate =
                                                                       new Date(
                                                                               in.nextLong());
                                                               break;
                                                           case "expenseValueRegistered":
                                                               expenseValueRegistered =
                                                                       new BigDecimal(
                                                                               in.nextString());
                                                               break;
                                                           default:
                                                               in.skipValue();
                                                               break;
                                                       }
                                                   }
                                                   in.endObject();

                                                   if (expenseRegisteredCategory != null &&
                                                           expenseRegisteredDate != null &&
                                                           expenseValueRegistered != null) {
                                                       expenseList.add(new Expense(
                                                               expenseRegisteredCategory,
                                                               expenseRegisteredDate,
                                                               expenseValueRegistered
                                                       ));
                                                   }
                                               }
                                               in.endArray();

                                               deserializedMap.put(registeredCategory, expenseList);
                                           }
                                           in.endObject();
                                           return deserializedMap;
                                       }

                                   })
            .create();

    /**
     * Nos permite agarrar un SharedPreferences de donde estamos trabajando, este generalmente es
     * uno para toda la aplicacion.
     */
    private SharedPreferences sharedPreferencesFromView;

    private static final String JSON_KEY_EXPENSE_CATEGORIES = "expenseCategories";
    private static final String JSON_KEY_EXPENSES_MAP = "expensesPerCategory";


    /**
     * Constructor de la clase SerializationEngine.
     * <p>
     * Este constructor inicializa la instancia de SharedPreferences donde la aplicacion almacena
     * informacion de las categorias e informacion de los expenses en formato JSON serializado.
     * SharedPreferences es configurado en el modo privado para que solamente pueda ser accedido por
     * esta aplicacion.
     * </p>
     *
     * @param contextComingFromView Contexto de la vista o actividad que crea la instancia de
     *                              SerializationEngine. Este parametro se usa para obtener el
     *                              acceso al almacenamiento de SharedPreferences.
     */
    public SerializationEngine(Context contextComingFromView) {
        this.sharedPreferencesFromView = contextComingFromView.getSharedPreferences(
                "expense_preferences_serialized", Context.MODE_PRIVATE);
    }


    /*! Metodos de serialization*/

    /**
     * Serializa el estado actual de la aplicacion guardando los datos en formato JSON dentro de
     * SharedPreferences. Este metodo guarda tanto las categorias de gastos como el mapa de gastos
     * asociados a esas categorias.
     *
     * <p>El metodo utiliza Gson para convertir los objetos de Java a representaciones
     * en cadena de texto formateadas en JSON que se almacenan en memoria persistente utilizando
     * SharedPreferences en el modo de edicion.</p>
     *
     * <p>En caso de fallo durante la serializacion, el metodo captura excepciones
     * y retorna un valor booleano que indica el resultado de la operacion.</p>
     *
     * @param applicationDataPOJO Objeto que contiene los datos de la aplicacion, incluyendo las
     *                            categorias de gastos y el mapa de gastos asociados. Este parametro
     *                            es esencial ya que contiene los datos a serializar.
     * @return Un valor booleano donde {@code true} indica exito en la serializacion y {@code false}
     * indica que ocurrio un error durante el proceso.
     * @throws NullPointerException Si {@code applicationDataPOJO} es {@code null}.
     * @throws org.json.JSONException      Si ocurre un error al procesar JSON durante la serializacion.
     * @see ApplicationDataPOJO
     */
    public final boolean serializeApplicationState(ApplicationDataPOJO applicationDataPOJO) {
        try{
            //? 1. Cargamos los SharedPreferences abriendolos para edicion con el metodo edit
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferencesFromView.edit();

            //? 2.1 Serializamos las categorias, como es una lista es mucho mas facil el proceso
            // ya que cada clase tiene sus parametros marcados para serializacion
            String serializedCategories =
                    this._gsonInstanceForSerialization
                            .toJson(applicationDataPOJO
                                            .getApplicationDataPojoExpenseCategories());
            //? 2.1.1 Escribimos la string a las preferencias y aplicamos (este es asincrono)
            sharedPreferencesEditor.putString(JSON_KEY_EXPENSE_CATEGORIES, serializedCategories);
            sharedPreferencesEditor.apply();
            //? 2.2 Serializamos los expenses, en este caso usamos un TypeToken, clase de Gson
            // que nos permite definir un modelo de serializacion unico para una clase, esto nos
            // permite enchufarle funcionalidad a la serializacion para que cuando se serialize
            // lo haga con un formato predefinido.
            String serializedExpensesMap =
                    this._gsonInstanceForSerialization.toJson(
                            applicationDataPOJO.getApplicationDataPojoExpensesMap(),
                            new TypeToken<Map<String, List<Expense>>>(){}.getType());
            sharedPreferencesEditor.putString(JSON_KEY_EXPENSES_MAP, serializedExpensesMap);

            //? 2.2.1 Escribimos los datos y esta vez sincronicamente
            return sharedPreferencesEditor.commit();
        }catch (Exception e){
            Log.e("Error Code 0x0001 - [Raised]", "Error Code 0x0001 - [Raised] Error al intentar " +
                    "serializar el estado de la aplicacion");
            return false;
        }
    }


    /**
     * Metodo que permite deserializar el estado previo de la aplicacion, cargando los datos
     * almacenados en las SharedPreferences en sus estructuras correspondientes dentro de
     * {@link ApplicationDataPOJO}.
     *
     * <p>Utiliza Gson para leer y convertir las cadenas en formato JSON, que
     * representan las categorias y los gastos mapeados por categoria, a objetos de Java. La
     * informacion deserializada se almacena en el singleton {@link ApplicationDataPOJO}.</p>
     *
     * <h3>Forma de Operacion</h3>
     * <ol>
     *     <li>Se obtienen los datos en JSON almacenados en SharedPreferences.</li>
     *     <li>Se convierten las categorias al tipo {@code List<ExpenseCategory>}.</li>
     *     <li>Se convierte el mapa de gastos al tipo {@code Map<String, List<Expense>>}.</li>
     *     <li>Se asignan los datos deserializados al singleton {@link ApplicationDataPOJO}.</li>
     * </ol>
     *
     * @return {@code true} si la deserializacion fue exitosa, {@code false} si ocurrio un error
     * un error durante la operacion.
     * @throws NullPointerException si una de las claves de SharedPreferences contiene un valor nulo
     *                              no esperado.
     * @throws JsonSyntaxException  si los datos almacenados en JSON tienen un formato incorrecto o
     *                              incompatible con las clases esperadas.
     * @see ApplicationDataPOJO
     */
    public final boolean deserializePrevApplicationState() {
        try {
            ApplicationDataPOJO applicationDataPOJO =
                    ApplicationDataPOJO.getApplicationDataPOJOInstance();
            //? Deserializamos las categorias
            String deserializedCategories =
                    sharedPreferencesFromView.getString(JSON_KEY_EXPENSE_CATEGORIES, "{}");
            Type tipoDeDatoAConvertir = new TypeToken<ArrayList<ExpenseCategory>>() {
            }.getType();
            List<ExpenseCategory> categories =
                    this._gsonInstanceForSerialization.fromJson(deserializedCategories,
                                                                tipoDeDatoAConvertir);
            applicationDataPOJO.setApplicationDataPojoExpenseCategories(categories);
            //? Deserializamos los expenses
            String deserializedExpenses =
                    sharedPreferencesFromView.getString(JSON_KEY_EXPENSES_MAP, "[]");
            tipoDeDatoAConvertir = new TypeToken<Map<String, List<Expense>>>() {
            }.getType();
            HashMap<String, List<Expense>> expenses =
                    this._gsonInstanceForSerialization.fromJson(deserializedExpenses,
                                                                tipoDeDatoAConvertir);
            applicationDataPOJO.setApplicationDataPojoExpensesMap(expenses);


            //? Si llegamos hasta aqui entonces todo bien y regresamos una key
            return true;
        } catch (Exception e) {
            Log.e("Error Code 0x0001 - [Raised]", "Error Code 0x0001 - [Raised] Error al intentar " +
                    "deserializar el estado de la aplicacion");
            return false;
        }
    }
}
