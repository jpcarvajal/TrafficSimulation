package movimiento

import json._
import cartesiano._
import vias._
import simulacion.Simulacion

abstract class Vehiculo (val placa:String,val origen:Interseccion, val destino:Interseccion, private var _velocidad:Velocidad) 
extends Movil(origen, _velocidad) with MovimientoUniforme {
 
 private var _punto:Punto = origen
 
 def punto=_punto
 
 //para obtener la ruta
  def n(outer:Interseccion):Simulacion.grafoVia.NodeT=Simulacion.grafoVia get outer
  //ruta es una lista de intersecciones
  var ruta =(n(origen) shortestPathTo n(destino)).get.nodes.toList
  
  def actualizarAngulo(a:Punto,b:Punto,v:Velocidad):Unit={
   //se haya el ángulo entre el vector (b-a) y el vector (0,1) siendo a el punto actual y b al que se quiere ir
    val vector = new Punto(b.x-a.x,b.y-a.y)
    if(vector.y>0){ 
    v.angulo=new Angulo(scala.math.acos(vector.x/Punto.distancia(vector,Punto(0,0)))*180/math.Pi)
    }
    //se le suma 180 grados en caso de que el vector esté en los cuadrantes 3 o 4
    else{
      v.angulo=new Angulo((scala.math.acos(vector.x/Punto.distancia(vector,Punto(0,0)))*180/math.Pi)+180)
    }
 }
 
 Simulacion.vehiculos :+=this

}

object Vehiculo{
  
  val r = scala.util.Random
  
  val letras = ('A' to 'Z')
  
  val digitos = ('0' to '9')  
  
  def crearVehiculo(vMin:Int, vMax:Int, proporciones:Array[String], intersecciones:Array[Interseccion]):Vehiculo={
    def definirTipo(n:String)= n match{
      //se usa el constructor que no recibe placa, en cada clase estará definido como se crean
      //y se envía una interseccion origen y una destino (no se verifica que sean diferentes)
      //el angulo de la velocidad es 0 por defecto
      case "carro" => new Carro(Carro.generarPlaca,
            intersecciones(r.nextInt(intersecciones.length)),
            intersecciones(r.nextInt(intersecciones.length)),
            new Velocidad(vMin+r.nextInt(vMax-vMin)))
      case "moto" => new Moto(Moto.generarPlaca,
            intersecciones(r.nextInt(intersecciones.length)),
            intersecciones(r.nextInt(intersecciones.length)),
            new Velocidad(vMin+r.nextInt(vMax-vMin)))
      case "mototaxi" => new MotoTaxi(MotoTaxi.generarPlaca,
            intersecciones(r.nextInt(intersecciones.length)),
            intersecciones(r.nextInt(intersecciones.length)),
            new Velocidad(vMin+r.nextInt(vMax-vMin)))
      case "camion" => new Camion(Camion.generarPlaca,
            intersecciones(r.nextInt(intersecciones.length)),
            intersecciones(r.nextInt(intersecciones.length)),
            new Velocidad(vMin+r.nextInt(vMax-vMin)))
      case "bus" => new Bus(Bus.generarPlaca,
            intersecciones(r.nextInt(intersecciones.length)),
            intersecciones(r.nextInt(intersecciones.length)),
            new Velocidad(vMin+r.nextInt(vMax-vMin)))
    }
  //se genera un vehículo dependiendo del tipo.
  definirTipo(tipo)
  }
}
