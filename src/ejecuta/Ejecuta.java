/*
 Clase que recibe como argumentos el comando y las opciones del comando 
 que se quiere ejecutar. Crea un proceso hijo que ejecute el comando con 
 las opciones correspondientes mostrando un mensaje de error en el caso de 
 que no se realizase correctamente la ejecución. 
 El padre debe esperar a que el hijo termine de informar si se produjo alguna 
 anomalía en la ejecución del hijo.
*/
package ejecuta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author fuerm
 */
public class Ejecuta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String salida = null;
        
         if (args.length <= 0) {
            System.err.println("Se necesita un programa a ejecutar");
            System.exit(-1);
        }
         
        //Runtime es un interface con el entorno de ejecución
        //El getRuntime me genera un Static Runtime
        Runtime runtime = Runtime.getRuntime();
         try {
             //Ejecutamos el comado especificado en args en un proceso separado
            Process process = runtime.exec(args);
            
            /*
            Por defecto los subprocesos creados no tienen su porpia terminal o consola.
            Todas las I/O estandar serán redireccionadas al proceso padre. Donde
            Estas I/O redireccionadas podrán ser accedidas via streams obtenidos
            usando los métodos  getOutputStream(), getInputStream(), 
            and getErrorStream().
            El proceso padre usa estos streams para alimentar la entrada y 
            obtener la salida de los subprocesos. Es coveniente usar Buffers para
            prevenir tamaños pequeños de los buffers de las plataformas nativas.
            */
           
            //Redireccionamos la salida del proceso hijo como entrada del proceso padre
            BufferedReader stdInput = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            
            //Si el comando ejecutado en el subproceso process tiene salida, la mostramos
             if((salida=stdInput.readLine()) != null){
                System.out.println("Comando ejecutado Correctamente");
                while ((salida=stdInput.readLine()) != null){
                    System.out.println(salida);
                }
            }else{
                System.out.println("No se a producido ninguna salida");
            }
            
           
            process.waitFor();
            int retorno = process.waitFor();
            System.out.println("La ejecución de "
                    + Arrays.toString(args) + " devuelve " + retorno);
           
        } catch (IOException ex) {
            System.err.println("Excepción de E/S");
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.err.println("El proceso hijo finalizó de forma incorrecta");
            System.exit(-1);

        }
    }
    
}
