package co.com.fuelcontrol.facturamanual.Controllers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.widget.Toast;
import co.com.fuelcontrol.facturamanual.Models.Consecutivo;
import co.com.fuelcontrol.facturamanual.Models.EncabezadoFactura;
import co.com.fuelcontrol.facturamanual.Models.Factura;
import com.co.fuelcontrol.facturamanual.R;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Formulario extends AppCompatActivity {

    private String macDispositivo;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private DecimalFormat formatea = new DecimalFormat("###,###.##");
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private boolean estado = false;
    private Handler bluetoothIn;
    private final int handlerState = 0;
    private int contador = 0;
    private Factura factura;
    private Consecutivo consecutivo;
    private int numConsecutivo = 0;
    private boolean editar;
    private int btn = 0;
    private int volumenValido;
    private int numfacturacopia = 0;
    private boolean primeraImpresion;
    private boolean aumentarNumero = false;
    private Date date = new Date();
    private DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");



    boolean hayPunto = false;
    int contadorPuntos = 0;
    int posicionDelPunto = 0;
    String parteDecimal = null;




    //TextInputLayout
    private TextInputLayout tilNombreEmpresa;
    private TextInputLayout tilNombreEds;
    private TextInputLayout tilNitEds;
    private TextInputLayout tilCel;
    private TextInputLayout tilDireccion;
    private TextInputLayout tilCiudad;
    private TextInputLayout tilNombre;
    private TextInputLayout tilCedula;
    private TextInputLayout tilPlaca;
    private TextInputLayout tilKilometraje;
    private TextInputLayout tilHorometro;
    private TextInputLayout tilVolumen;
    private TextInputLayout tilPpu;
    private TextInputLayout tilProducto;


    //TextInputEditText
    private TextInputEditText tiNombreEmpresa;
    private TextInputEditText tiNombreEds;
    private TextInputEditText tiNitEds;
    private TextInputEditText tiTelEds;
    private TextInputEditText tiDireccion;
    private TextInputEditText tiCiudad;
    private TextInputEditText tiNombre;
    private TextInputEditText tiCedula;
    private TextInputEditText tiPlaca;
    private TextInputEditText tiKilometraje;
    private TextInputEditText tiHorometro;
    private TextInputEditText tiVolumen;
    private TextInputEditText tiPpu;
    private TextInputEditText tiProducto;

    //String con el recibo completo
    private String recibo;

    //Conexion Bluetooth
    private ConexionThread MyConexionBT;

    //Botones para guardar y editar factura
    private Button btnEdit;
    private Button btnEnviar;

    //Instancia servicios de factura
    private ServicioFactura servicioFactura;
    private ServicioConsecutivo servicioConsecutivo;

    //lista de facturas
    private EncabezadoFactura[] encabezadoFacturas;

    //factura individual
    private EncabezadoFactura factura1;
    private Consecutivo consecutivo1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Recivo la mac del dispositivo para realizar la conexion
        macDispositivo = getIntent().getStringExtra("device");

        //Instancia de los TextInputLayout
        tilNombreEmpresa = (TextInputLayout) findViewById(R.id.tilNombreEmpresa);
        tilNombreEds = (TextInputLayout)findViewById(R.id.tilNombreEds);
        tilNitEds = (TextInputLayout)findViewById(R.id.tilNitEds);
        tilCel = (TextInputLayout)findViewById(R.id.tilCel);
        tilDireccion = (TextInputLayout)findViewById(R.id.tilDireccion);
        tilCiudad= (TextInputLayout)findViewById(R.id.tilCiudad);
        tilNombre = (TextInputLayout)findViewById(R.id.tilNombre);
        tilCedula = (TextInputLayout)findViewById(R.id.tilCedula);
        tilPlaca= (TextInputLayout)findViewById(R.id.tilPlaca);
        tilKilometraje = (TextInputLayout)findViewById(R.id.tilKilometraje);
        tilHorometro = (TextInputLayout)findViewById(R.id.tilHorometro);
        tilVolumen = (TextInputLayout)findViewById(R.id.tilVolumen);
        tilPpu = (TextInputLayout)findViewById(R.id.tilPpu);
        tilProducto = (TextInputLayout)findViewById(R.id.tilProducto);

        //Instancia de los botones
        btnEdit = (Button)findViewById(R.id.btnEdit);
        //Envía la factura para su impresión
        btnEnviar = (Button)findViewById(R.id.btnEnviar);

        //Instancia de los TextInputEditText
        tiNombreEmpresa = (TextInputEditText)findViewById(R.id.tiNombreEmpresa);
        tiNombreEds = (TextInputEditText)findViewById(R.id.tiNombreEds);
        tiNitEds = (TextInputEditText)findViewById(R.id.tiNitEds);
        tiTelEds = (TextInputEditText)findViewById(R.id.tiTelEds);
        tiDireccion = (TextInputEditText)findViewById(R.id.tiDireccion);
        tiCiudad = (TextInputEditText)findViewById(R.id.tiCiudad);
        tiNombre = (TextInputEditText)findViewById(R.id.tiNombre);
        tiCedula = (TextInputEditText)findViewById(R.id.tiCedula);
        tiPlaca = (TextInputEditText)findViewById(R.id.tiPlaca);
        tiKilometraje = (TextInputEditText)findViewById(R.id.tiKilometraje);
        tiHorometro = (TextInputEditText)findViewById(R.id.tiHorometro);
        tiVolumen = (TextInputEditText)findViewById(R.id.tiVolumen);
        tiPpu = (TextInputEditText)findViewById(R.id.tiPpu);
        tiProducto = (TextInputEditText)findViewById(R.id.tiProducto);

        //Intenta conectarse tres veces al disposivo
        conectar();
        int cont = 0;
        while (!estado){
            conectar();
            cont ++;
            if(cont==2){
                Toast.makeText(Formulario.this, "Error de conexión o dispositivo no compatible", Toast.LENGTH_SHORT).show();
                break;
            }
        }


        //Incia, configura y envía una instancia para utilizar los metodos de Servicio factura
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("ReciboDistracom")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        servicioFactura = new ServicioFactura(Realm.getDefaultInstance());
        servicioConsecutivo = new ServicioConsecutivo(Realm.getDefaultInstance());

        //Rellena los TextInputEditText con el encabezado de la factura
        resetearTextos();



        //Deshabilita los TextInputEditText para que no se puedan modificar
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               encabezadoFacturas = servicioFactura.obtenerFacturas();
                btn += 1;
                if (btn % 2 != 0) {
                    habilitarTextos();
                    editar = true;
                    btnEdit.setText("Guardar");
                } else {
                        factura1 = servicioFactura.obtenerFacturaPorId(1);
                        servicioFactura.actualizarFactura(factura1,
                                tilNombreEmpresa.getEditText().getText().toString().toLowerCase(),
                                tilNombreEds.getEditText().getText().toString().toLowerCase(),
                                tilNitEds.getEditText().getText().toString().toLowerCase(),
                                tilCel.getEditText().getText().toString().toLowerCase(),
                                tilDireccion.getEditText().getText().toString().toLowerCase(),
                                tilCiudad.getEditText().getText().toString().toLowerCase());
                        if( validarEncabezado() == 6) {
                            deshabilitarTextos();
                            editar = false;
                            btnEdit.setText("Editar Encabezado");
                        }

                      }
                }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editar){
                    Toast.makeText(getApplicationContext(),"Guarde el encabezado de la factura",Toast.LENGTH_LONG).show();
                    return;
                }

                factura = new Factura(tilNombre.getEditText().getText().toString().trim()
                        ,tilCedula.getEditText().getText().toString().trim()
                        ,tilPlaca.getEditText().getText().toString().trim()
                        ,tilKilometraje.getEditText().getText().toString().trim()
                        ,tilHorometro.getEditText().getText().toString().trim()
                        ,cambiarComa(tilVolumen.getEditText().getText().toString().trim())
                        ,tilPpu.getEditText().getText().toString().trim()
                        ,tilProducto.getEditText().getText().toString().trim());

                try {

                      if (validarFormulario() == 14) {
                        numfacturacopia ++;

                        factura1 = servicioFactura.obtenerFacturaPorId(1);
                        consecutivo1 = servicioConsecutivo.obtenerConsecutivoPorId(1);
                        numConsecutivo = consecutivo1.getConsecutivo();

                        recibo = "\n" + agregarEspacio(factura1.getNombreEmpresa()) + "\n"+
                                agregarEspacio(factura1.getNombreEds()) + "\n" +
                                agregarEspacio("Nit: " + factura1.getNitEds()) + "\n" +
                                agregarEspacio("Tel: " + factura1.getTelefono()) + "\n" +
                                agregarEspacio("Dir: " + factura1.getDireccion()) + "\n" +
                                agregarEspacio(factura1.getCiudad()) +
                                "\n-----------------------------\n" +
                                "Fecha: " + fechaHora.format(date) +
                                "\nnumero de recibo " + String.format("%05d", numConsecutivo) +
                                "\n-----------------------------\n" +
                                agregarEspacio("Cantidad -- Producto -- Ppu") + "\n" +
                                agregarEspacio(factura.getVarios()) +
                                "\n-----------------------------\n" +
                                "Nombre: " + factura.getNombre() +
                                "\nNit/cedula: " + factura.getCedula() +
                                "\nPlaca: " + factura.getPlaca() +
                                "\nHorometro: " + factura.getHorometro() +
                                "\nKm: " + factura.getKilometraje() +
                                "\n-----------------------------\n" +
                                "Total: $  " + formatea.format(factura.getTotal())+
                                "\n-----------------------------\n"+
                                "\n "+
                                "\nEntregado por: _________________\n"+
                                "\n  "+
                                "\nRecibido por: __________________\n"+
                                "\n "+
                                "\n "+
                                "\n "+
                                "\n "+
                                "\n "+
                                "\n ";

                        if(numfacturacopia%2 != 0) {
                            dialogo();
                        }else {

                            if (estado) {
                                String dato = recibo.toUpperCase();
                                consecutivo1 = servicioConsecutivo.obtenerConsecutivoPorId(1);
                                numConsecutivo = consecutivo1.getConsecutivo();
                                if(numConsecutivo == 99999){
                                servicioConsecutivo.actualizarConsecutivo(consecutivo1,1);
                               }
                                numConsecutivo ++;
                                servicioConsecutivo.actualizarConsecutivo(consecutivo1,numConsecutivo);
                                MyConexionBT.write(dato);
                                primeraImpresion = false;
                                    vaciarCampos();
                                    habilitarFactura();
                                    btnEdit.setEnabled(true);
                                    btnEnviar.setText("Generar factura");
                                    numfacturacopia = 0;
                            } else {
                                Toast.makeText(Formulario.this, "Solo se puede enviar datos si el dispositivo esta vinculado", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Llene todos los campos para imprimir",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void conectar(){

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        //Direccion mac del dispositivo a conectar
        BluetoothDevice device = btAdapter.getRemoteDevice(macDispositivo);

        try
        {
            //Crea el socket sino esta conectado
            if(!estado)
            {
                btSocket = createBluetoothSocket(device);

                estado = btSocket.isConnected();
            }

        }
        catch (IOException e)
        {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }

        // Establece la conexión con el socket Bluetooth.
        try
        {
            //Realiza la conexion si no se a hecho
            if(!estado)
            {
                btSocket.connect();
                estado = true;
                MyConexionBT = new ConexionThread(btSocket);
                MyConexionBT.start();
                Toast.makeText(Formulario.this, "Conexion Realizada Exitosamente", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(Formulario.this, "Ya esta vinculado", Toast.LENGTH_SHORT).show();

            }
        }

        catch (IOException e)
        {
            try {
                Toast.makeText(Formulario.this, "Error:", Toast.LENGTH_SHORT).show();
              //  Toast.makeText(Formulario.this, e.toString(), Toast.LENGTH_SHORT).show();
                btSocket.close();
            }
            catch (IOException e2) {}
        }


    }



    //Crea el socket
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }


    //Se debe crear una sub-clase para tambien heredar los metodos de CompaActivity y Thread juntos
//Ademas  en Run se debe ejecutar el subproceso(interrupcion)
    private class ConexionThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConexionThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                // Se mantiene en modo escucha para determinar el ingreso de datos
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Enviar los datos
        public void write(String input) {
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }

    //agrega espacios al encabezado para centrar los textos
    public String agregarEspacio(String argumento){

        int cadena = argumento.length();
        int total = 32;
        String espacio = "";

        if(cadena < total){
            if(cadena != 0){
            int resultado = (int) Math.ceil((total - cadena)/2);
            int cont=1;
            while(cont<=resultado){
                espacio += " ";
                cont++;
            }
            return espacio+argumento;
        }}else if(cadena > total && cadena < 64){
         String nuevaLinea = argumento.substring(32,cadena);
         int resultado = (int) Math.ceil((total - nuevaLinea.length())/2);
            int cont=1;
            while(cont<=resultado){
                espacio += " ";
                cont++;
            }
            argumento = argumento.substring(0,32)+espacio+nuevaLinea;
            return argumento;
        }
        return argumento;

    }




    //valida el encabezado de la factura
    public int validarEncabezado(){
        int encabezadoValido = 0;
        factura1 = servicioFactura.obtenerFacturaPorId(1);
        if (factura1.getNombreEmpresa().isEmpty() || tilNombreEmpresa.getEditText().getText().toString().length() == 0) {
            tilNombreEmpresa.setError("Este campo no puede estár vacio");
        }else {
            tilNombreEmpresa.setError(null);
            encabezadoValido ++;

        }if(factura1.getNombreEds().isEmpty() || tilNombreEds.getEditText().getText().toString().length() == 0){
            tilNombreEds.setError("Este campo no puede estár vacio");
        }else{
            tilNombreEds.setError(null);
            encabezadoValido ++;

        }if(factura1.getNitEds().isEmpty() || tilNitEds.getEditText().getText().toString().length() == 0){
            tilNitEds.setError("Este campo no puede estár vacio");
        }else{
            tilNitEds.setError(null);
            encabezadoValido ++;

        }if(factura1.getTelefono().isEmpty() || tilCel.getEditText().getText().toString().length() == 0){
            tilCel.setError("Este campo no puede estár vacio");
        }else{
            tilCel.setError(null);
            encabezadoValido ++;

        }if(factura1.getDireccion().isEmpty() || tilDireccion.getEditText().getText().toString().length() == 0){
            tilDireccion.setError("Este campo no puede estár vacio");
        }else{
            tilDireccion.setError(null);
            encabezadoValido ++;

        }if (factura1.getCiudad().isEmpty() || tilCiudad.getEditText().getText().toString().length() == 0){
            tilCiudad.setError("Este campo no puede estár vacio");
        }else{
            tilCiudad.setError(null);
            encabezadoValido ++;
        }
        return encabezadoValido;
    }




    //Valida que los campos no estén vacíos en la factura
    public int validarFormulario() {
        factura1 = servicioFactura.obtenerFacturaPorId(1);
        String cantidad = tilVolumen.getEditText().getText().toString();
         int valido = 0;
        if (factura1.getNombreEmpresa().isEmpty()) {
            tilNombreEmpresa.setError("Este campo no puede estár vacio");
        }else {
            tilNombreEmpresa.setError(null);
            valido++;
        }if(factura1.getNombreEds().isEmpty()){
            tilNombreEds.setError("Este campo no puede estár vacio");
        }else{
            tilNombreEds.setError(null);
            valido++;
        }if(factura1.getNitEds().isEmpty()){
            tilNitEds.setError("Este campo no puede estár vacio");
        }else{
            tilNitEds.setError(null);
            valido++;
        }if(factura1.getTelefono().isEmpty()){
            tilCel.setError("Este campo no puede estár vacio");
        }else{
            tilCel.setError(null);
            valido++;
        }if(factura1.getDireccion().isEmpty()){
            tilDireccion.setError("Este campo no puede estár vacio");
        }else{
            tilDireccion.setError(null);
            valido++;
        }if (factura1.getCiudad().isEmpty()){
            tilCiudad.setError("Este campo no puede estár vacio");
        }else{
            tilCiudad.setError(null);
            valido++;
        }if(tilNombre.getEditText().getText().toString().length() == 0){
            tilNombre.setError("Este campo no puede estár vacio");
        }else{
            tilNombre.setError(null);
            valido++;
        }if(tilCedula.getEditText().getText().toString().length() == 0){
            tilCedula.setError("Este campo no puede estár vacio");
        }else{
            tilCedula.setError(null);
            valido++;
        }if(tilPlaca.getEditText().getText().toString().length() == 0){
            tilPlaca.setError("Este campo no puede estár vacio");
        }else{
            tilPlaca.setError(null);
            valido++;
        }if(tilKilometraje.getEditText().getText().toString().length() == 0){
            tilKilometraje.setError("Este campo no puede estár vacio");
        }else{
            tilKilometraje.setError(null);
            valido++;
        }if(tilHorometro.getEditText().getText().toString().length() == 0){
            tilHorometro.setError("Este campo no puede estár vacio");
        }else{
            tilHorometro.setError(null);
            valido++;
        }if(tilVolumen.getEditText().getText().toString().length() == 0) {
            tilVolumen.setError("Este campo no puede estár vacio");
        }else{
                valido++;
                tilVolumen.setError(null);
        }if(tilPpu.getEditText().getText().toString().length() == 0){
            tilPpu.setError("Este campo no puede estár vacio");
        }else{
            tilPpu.setError(null);
            valido++;
        }if(tilProducto.getEditText().getText().toString().length() == 0){
            tilProducto.setError("Este campo no puede estár vacio");
        }else{
            tilProducto.setError(null);
            valido++;
        }

        return valido;
    }


    //deshabilita los campos para que no se pueda escribir
    public void deshabilitarTextos(){
        tilNombreEmpresa.setFocusable(false);
        tilNombreEmpresa.setFocusable(false);
        tiNombreEmpresa.setEnabled(false);
        tiNombreEds.setEnabled(false);
        tiNitEds.setEnabled(false);
        tiTelEds.setEnabled(false);
        tiDireccion.setEnabled(false);
        tiCiudad.setEnabled(false);
    }


    //habilita los campos para que se puedan editar
    public void habilitarTextos(){
        tiNombreEmpresa.setEnabled(true);
        tiNombreEds.setEnabled(true);
        tiNitEds.setEnabled(true);
        tiTelEds.setEnabled(true);
        tiDireccion.setEnabled(true);
        tiCiudad.setEnabled(true);
    }


    //trae una factura creada, sino existe crea una con campos nulos
    public void resetearTextos(){
       encabezadoFacturas = servicioFactura.obtenerFacturas();
        if(encabezadoFacturas.length > 0) {
            deshabilitarTextos();
            factura1 = servicioFactura.obtenerFacturaPorId(1);
            tiNombreEmpresa.setText(factura1.getNombreEmpresa());
            tiNombreEds.setText(factura1.getNombreEds());
            tiNitEds.setText(factura1.getNitEds());
            tiTelEds.setText(factura1.getTelefono());
            tiDireccion.setText(factura1.getDireccion());
            tiCiudad.setText(factura1.getCiudad());

        }else{
            deshabilitarTextos();
            editar=false;
            servicioFactura.crearFactura(1, null,
                   null,
                    null,
                    null,
                    null,
                    null);

            servicioConsecutivo.crearConsecutivo(1,1);
            consecutivo1 = servicioConsecutivo.obtenerConsecutivoPorId(1);
            numConsecutivo = consecutivo1.getConsecutivo();
        }
    }

    // pone los campos del cuerpo de la factura vacíos
    public void vaciarCampos(){
        tiNombre.setText(null);
        tiCedula.setText(null);
        tiPlaca.setText(null);
        tiKilometraje.setText(null);
        tiHorometro.setText(null);
        tiVolumen.setText(null);
        tiPpu.setText(null);
        tiProducto.setText(null);
    }

    // deshabilita los campos del cuerpo de la factura
    public void desHabilitarFactura(){
        tilNombre.setEnabled(false);
        tilCedula.setEnabled(false);
        tilPlaca.setEnabled(false);
        tilKilometraje.setEnabled(false);
        tilHorometro.setEnabled(false);
        tilVolumen.setEnabled(false);
        tilPpu.setEnabled(false);
        tilProducto.setEnabled(false);
    }

    // habilita los campos del cuerpo de la factura
    public void habilitarFactura(){
        tilNombre.setEnabled(true);
        tilCedula.setEnabled(true);
        tilPlaca.setEnabled(true);
        tilKilometraje.setEnabled(true);
        tilHorometro.setEnabled(true);
        tilVolumen.setEnabled(true);
        tilPpu.setEnabled(true);
        tilProducto.setEnabled(true);
    }

    //muestra una ventana de dialogo para rectificar que los datos de la factura sean correctos
    // para imprimir la copia de la factura no se llama este metodo
    public void dialogo(){
        new AlertDialog.Builder(this)
                .setTitle("Está de seguro de imprimir el recibo ?")
                .setMessage(recibo.toUpperCase())
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        numfacturacopia --;
                        contador --;
                    }
                })
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (estado) {
                            String dato = recibo.toUpperCase();
                            MyConexionBT.write(dato);
                            primeraImpresion = true;

                            if(numfacturacopia%2 != 0) {
                                btnEnviar.setText("Generar copia factura");
                                desHabilitarFactura();
                                btnEdit.setEnabled(false);
                            }

                        } else {
                            Toast.makeText(Formulario.this, "Solo se puede enviar datos si el dispositivo esta vinculado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create().show();
    }


    // despues de generar la factura sin copia, si presiona el boton atrás se aumenta el numero de consecutivo del recibo
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        aumentarNumero = true;
        if(numfacturacopia%2 != 0) {
            if(primeraImpresion){
                consecutivo1 = servicioConsecutivo.obtenerConsecutivoPorId(1);
                numConsecutivo = consecutivo1.getConsecutivo();
                numConsecutivo ++;
                servicioConsecutivo.actualizarConsecutivo(consecutivo1,numConsecutivo);
                Toast.makeText(getApplicationContext(),"No se generó la copia de la factura",Toast.LENGTH_LONG).show();
            }

        }
    }


    // despues de generar la factura sin copia, si se cierra la app se aumenta el numero de consecutivo del recibo
    @Override
    protected void onStop() {
        super.onStop();
        if (numfacturacopia % 2 != 0) {
            if (primeraImpresion && aumentarNumero != true) {
                consecutivo1 = servicioConsecutivo.obtenerConsecutivoPorId(1);
                numConsecutivo = consecutivo1.getConsecutivo();
                numConsecutivo++;
                servicioConsecutivo.actualizarConsecutivo(consecutivo1, numConsecutivo);
                vaciarCampos();
                habilitarFactura();
                btnEdit.setEnabled(true);
                btnEnviar.setText("Generar factura");
                numfacturacopia = 0;
                Toast.makeText(getApplicationContext(), "No se generó la copia de la factura", Toast.LENGTH_LONG).show();
            }

        }
    }

    public String cambiarComa(String volumen){
        String volumenFormateado = volumen.replace(",",".");
        return volumenFormateado;
    }
}
