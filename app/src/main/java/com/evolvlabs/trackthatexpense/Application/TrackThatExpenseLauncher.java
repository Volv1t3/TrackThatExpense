package com.evolvlabs.trackthatexpense.Application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.evolvlabs.trackthatexpense.PersistencyEngine.ApplicationDataPOJO;
import com.evolvlabs.trackthatexpense.R;
import com.evolvlabs.trackthatexpense.Utils.SerializationEngine;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

/**
 * @author : Paulo Cantos, Santigo Arellano
 * @date : 20-Mar-2025
 * @description: El presente archivo implementa la interconexion principal de los components
 * visuales de la applicacion TrackThatExpense. La idea de esta clase es presentar una
 * implementacion clara y simple del control programatico de la aplicacion, manteniendo separados
 * los controladores y modelos de datos en sus respectivos modulos y paquetes.
 */
public class TrackThatExpenseLauncher extends AppCompatActivity {


    /**
     * Motor de la Serializacion y Deserializacion, es util para crear la instancia Singleton de 
     * los datos, y ademas manejar los datos en cambios de fragmento
     */
    private SerializationEngine serializationEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Esto es base, ya que en esta seccion debemos de llamar a lo que tenga que ver con
         * inflar la UI, despues de esta seccion podemos interactura con los componentes para
         * manejar el estado de nuestra app!
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_that_expense_launcher);

        /*
         * Con este metodo llamamos a que el programa deserialize la informacion pertinente del
         * SharedPreferences, pero si no tenemos nada este no carga informacion al Singleton.
        */
        initializeInternalApplicationData();
        /*
         * Usamos este metodo para cargar el estado de la UI, variando los componentes
         * programaticamente en caso de que exista o no informacion.
         */
        initializeExternalApplicationUI();
    }


    /**
     * <body style="color: white;">
     * Este metodo inicializa los datos internos de la aplicacion usando una instancia de
     * SerializationEngine para manejar la serializacion y deserializacion del estado de la
     * aplicacion.
     *
     * <p>Este metodo no toma parametros ni devuelve valores explicitamente. Se asegura de que el
     * singleton {@code ApplicationDataPOJO} contenga datos deserializados si estos existen, y de lo
     * contrario, maneja el caso en que no haya datos iniciales.</p>
     *
     * <p><b>Conceptos:</b> Usa un singleton para centralizar la informacion de datos principal
     * en la aplicacion, habilitando un punto unificado para manejar la informacion entre
     * fragmentos. Tambien utiliza el motor de serializacion para cargar y guardar estos datos con
     * persistencia.</p>
     *
     * @throws Exception si ocurre un error al inicializar {@code SerializationEngine} o durante el
     *                   proceso de deserializacion.
     *                   </body>
     */
    private void initializeInternalApplicationData() {
        try {
            this.serializationEngine = new SerializationEngine(
                    this.getApplicationContext());
            boolean resultadoDeCarga = this.serializationEngine.deserializePrevApplicationState();
            if (!resultadoDeCarga) {
                Log.w("TrackThatExpense", "No se pudo cargar la informacion previa de la " +
                        "aplicacion, esto puede ser porque nunca se la ha usado antes");
            }

        } catch (Exception e) {
            Log.e("TrackThatExpense", "Error al inicializar la aplicacion: " + e.getMessage());
        }
    }

    /**
     * <body style="color: white;">
     * Este metodo inicializa y configura la Interfaz de Usuario externa (UI) de la aplicacion,
     * conectando los componentes visuales principales como el NavigationView y el NavHostFragment a
     * traves del NavController.
     *
     * <p><b>Funcionamiento:</b> Usando el componente de navegacion de Android, este metodo
     * interactua con las vistas predefinidas en el layout de la aplicacion para conectar
     * programaticamente el sistema de navegacion. El {@code NavHostFragment} actua como un
     * contenedor para los fragmentos asociados, mientras que el {@code NavigationView} funciona
     * como menu lateral para la navegacion programatica entre pantallas.</p>
     *
     * <p><b>Conceptos claves:</b> Implica el uso de {@code NavHostFragment} para manejar
     * los fragmentos funcionales de la aplicacion y su navegacion. Tambien conecta esta
     * funcionalidad con el {@code NavigationView} mediante el {@code NavController}. Este metodo no
     * recibe parametros ni arroja excepciones especificas.</p>
     *
     * <p><b>Nota:</b> Es importante asegurarse de que los IDs de los componentes de UI
     * utilizados (por ejemplo, {@code R.id.nav_host_fragment} o {@code R.id.navigationView}) esten
     * correctamente definidos y coincidan con el layout asociado.</p>
     *
     * </body>
     */
    private void initializeExternalApplicationUI() {
        //? Primera seccion que definimos, tneemos que cargar el navghost fragment, y una vez
        // cargado esto estamos listos con la UI en esta seccion.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        //? 2. Conectamos el componente de navegacion de cada uno de los fragmentos que tenemos
        // (todos utilizan este id para su facil manejo) con el NavigationViev, el NavegationView
        // es un componente de Android que nos permite mostrar una vista de menu con nuestras
        // direcciones, en este caso usamos fragmnetos para la navegacion efectiva entre pestanas.
        NavigationView navigationView = findViewById(R.id.navigationView);
        //? 3. Conectamos el navController, es decir el controlador que viene desde el fragmento 
        // con el NavHostFramgnet que nos permite manejar tanto el stack de navegacion como la 
        // navegacion en si con el nav_graph.xml que creamos. El navgiationView es el componente 
        // de nuestra UI. este metodo se llama en el onCreate para inicializar la navegacion en 
        // un componente, notese que al ser un fragment loader, esta actividad se encarga de 
        // manejar toda la navegacion desde aqui
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    /*Estos tres metodos seran usados para la serializacion de la informacion, delegamos a un
    tercer metodo, pero en si manejamos las pausas en la UI con este estilo*/
    @Override
    protected void onStop() {
        super.onStop();
        saveApplicationState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveApplicationState();
    }

    @Override
    protected void onDestroy() {
        saveApplicationState();
        super.onDestroy();
    }


    /**
     * <body style="color: white;">
     * Este metodo guarda el estado actual de la aplicacion serializando los datos contenidos en la
     * instancia Singleton {@code ApplicationDataPOJO}.
     *
     * <p><b>Funcionamiento:</b> Usa el motor de serializacion {@code SerializationEngine}
     * para convertir el estado de la aplicacion en un formato persistente. Esto asegura que los
     * datos puedan ser restaurados en futuras sesiones.</p>
     *
     * <p>El metodo verifica si el proceso de serializacion fue exitoso. En caso de falla,
     * registra un mensaje de error en los logs para facilitar la deteccion de problemas.</p>
     *
     * <p><b>Conceptos claves:</b> Incluye el uso del Singleton para centralizar los datos
     * de la aplicacion y del motor de serializacion para asegurar la persistencia de informacion.
     * Maneja posibles excepciones durante el proceso de serializacion para evitar fallos
     * inesperados en la aplicacion.</p>
     *
     * @throws Exception si ocurre un error durante el proceso de serializacion.
     *                   </body>
     */
    private void saveApplicationState() {
        try {
            boolean estadoGuardado = this
                    .serializationEngine
                    .serializeApplicationState(ApplicationDataPOJO
                                                       .getApplicationDataPOJOInstance());
            if (!estadoGuardado) {
                Log.e("TrackThatExpense", "No se pudo guardar el estado de la aplicacion");
            }
        } catch (Exception e) {
            Log.e("TrackThatExpense", "Error al guardar el estado de la aplicacion: " + e.getMessage());
        }
    }
}
