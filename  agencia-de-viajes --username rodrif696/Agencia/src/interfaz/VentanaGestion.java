package interfaz;

import dominio.*;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

import java.awt.Point;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


public class VentanaGestion extends JFrame implements ActionListener{
     
     private JPanel panelInicio = null;
     private HandlerClientes panelClientes = null;
     private JPanel panelTrabajadores = null;
     private JPanel panelViajes = null;
     private JMenu menuArchivo = null;
     private JMenu menuOpciones = null;
     private JMenu menuAyuda = null;
     private JMenuBar barra = null;
     private JMenu subMenuGestion = null;
     private Sistema sistema = null;
     
     
     public VentanaGestion(Sistema sistemaP){
          
          super();
          this.setTitle("Gestion");
          this.setSize(900,750);
          this.setContentPane(getPanelInicio());
          this.setResizable(false);
          this.setJMenuBar(getMenuBarra());
          this.sistema = sistemaP;
          this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
          //uso Adapter para no implementar todos los metodos del Listener
          this.addWindowListener(new java.awt.event.WindowAdapter(){
               public void windowClosing(WindowEvent evento){
                    int respuesta = JOptionPane.showConfirmDialog(null, " �Desea salir?", "Confirmaci�n", JOptionPane.WARNING_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                         System.exit(0);
                    }
               }              
          });
     }
     
     private JPanel getPanelInicio(){
          
          if(panelInicio == null){
               panelInicio = new JPanel();
               panelInicio.setSize(900, 750); 
               panelInicio.setLayout(null); 
          }
          return panelInicio;
     }
     
     private HandlerClientes getPanelClientes(){
          
          if(panelClientes == null){
               panelClientes = new HandlerClientes();
          }
          return panelClientes;
     }
     
     private JPanel getPanelTrabajadores(){
          
          if(panelTrabajadores == null){
               panelTrabajadores = new HandlerTrabajadores();     
          }
          return panelTrabajadores;
     }
     
     private JPanel getPanelViajes(){
          
          if(panelViajes == null){
               panelViajes = new HandlerViajes();
          }
          return panelViajes;
     }
     
     private JMenu getMenuArchivo(){
          if(this.menuArchivo == null){
               menuArchivo = new JMenu("Archivo");
               menuArchivo.add(getSubMenuGestion());
               JMenuItem salir = new JMenuItem("Salir");
               salir.addActionListener(new java.awt.event.ActionListener(){
                    public void actionPerformed (ActionEvent evento){
                         int respuesta = JOptionPane.showConfirmDialog(null, " �Desea salir?", "Confirmaci�n", JOptionPane.WARNING_MESSAGE);
                         if (respuesta == JOptionPane.YES_OPTION){
                              System.exit(0);
                         }
                    }
               });
               menuArchivo.add(salir);                    
          }
          return this.menuArchivo;
     }
     
     private JMenu getMenuOpciones(){
          
          if(this.menuOpciones == null){
               menuOpciones = new JMenu("Opciones");
               menuOpciones.add(new JMenuItem("Preferencias"));               
          }
          return this.menuOpciones;
     }
     
     private JMenu getMenuAyuda(){
          
          if(this.menuAyuda == null){
               menuAyuda = new JMenu("Ayuda");
               menuAyuda.add(new JMenuItem("Manual"));
               menuAyuda.add(new JMenuItem("Acerca de..."));
               
          }
          return this.menuAyuda;
     }
     
     private JMenu getSubMenuGestion(){
          
          if(this.subMenuGestion == null){
               subMenuGestion = new JMenu("Gestion");
               poblarSubMenuGestion(subMenuGestion);
          }
          return this.subMenuGestion;
     }
     
     private void poblarSubMenuGestion(JMenu subMenu){
          
          JMenuItem clientes = new JMenuItem("Clientes");
          JMenuItem trabajadores = new JMenuItem("Trabajadores");
          JMenuItem viajes = new JMenuItem("Viajes");
          
          subMenu.add(clientes);
          subMenu.add(trabajadores);
          subMenu.add(viajes);
          
          clientes.addActionListener(this);
          trabajadores.addActionListener(this);
          viajes.addActionListener(this);          
     }
     
     private JMenuBar getMenuBarra(){
          
          if(this.barra == null){
               barra = new JMenuBar();
               barra.add(getMenuArchivo());
               barra.add(getMenuOpciones());
               barra.add(getMenuAyuda());
          }
          return this.barra;
     }
     
     
     public void actionPerformed(ActionEvent e){
          
          JMenuItem j = (JMenuItem)e.getSource();
          if(j.getText().equals("Clientes")){
               this.setContentPane(getPanelClientes());
          }
          if(j.getText().equals("Trabajadores")){
               this.setContentPane(getPanelTrabajadores());
          }
          if(j.getText().equals("Viajes")){
               this.setContentPane(getPanelViajes()); 
          } 
     }
     
     @SuppressWarnings("serial")
     private class HandlerClientes extends JPanel implements Observer, ActionListener, ListSelectionListener{
                    
          private JList listaClientes;
          private JList listaBuscados;
          private JList listaRealizados;
          private JTextField nombre;
          private JTextField apellido;
          private JTextField ci;
          private JButton agregar;
          private JButton eliminar;
          private JButton modificar;
          private JButton activos;
          private JButton espera;
          private JLabel listaClientesL;
          private JLabel nombreL;
          private JLabel apellidoL;
          private JLabel ciL;
          private JLabel buscadosL;
          private JLabel realizadosL;
          private DefaultListModel modeloListaClientes;
          private DefaultListModel modeloListaEnEspera;
          private DefaultListModel modeloBuscados;
          private DefaultListModel modeloRealizados;
          
          public HandlerClientes() {
               
               super();
               this.setSize(900, 750); 
               this.setLayout(null);
               
               modeloListaClientes = new DefaultListModel();
               modeloBuscados = new DefaultListModel();
               modeloRealizados = new DefaultListModel();
               modeloListaEnEspera = new DefaultListModel();
               cargarModelo(modeloListaClientes, sistema.getEmpresa().getListaClientes());
               cargarModelo(modeloListaEnEspera, sistema.getEmpresa().getListaDeEspera());
               listaClientes = new JList(modeloListaClientes);
               listaClientes.setSize(200,400);
               listaClientes.setLocation(75,85);
               listaClientes.addListSelectionListener(this);
               listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               this.add(listaClientes);
                              
               listaBuscados = new JList();
               listaBuscados.setSize(200,100);
               listaBuscados.setLocation(500,300);
               this.add(listaBuscados);
               
               listaRealizados = new JList();
               listaRealizados.setSize(200,100);
               listaRealizados.setLocation(500,450);
               this.add(listaRealizados);
               
               nombre = new JTextField();
               this.add(nombre);
               nombre.setSize(200,25);
               nombre.setLocation(500,100);
               
               apellido = new JTextField();
               this.add(apellido);
               apellido.setSize(200,25);
               apellido.setLocation(500,150);
               
               ci = new JTextField();
               this.add(ci);
               ci.setSize(200,25);
               ci.setLocation(500,200);
               
               agregar = new JButton("Agregar");
               this.add(agregar);
               agregar.setSize(120,25);
               agregar.setLocation(325,250);
               agregar.addActionListener(this);
               
               eliminar = new JButton("Eliminar");
               this.add(eliminar);
               eliminar.setSize(120,25);
               eliminar.setLocation(325,300);
               eliminar.addActionListener(this);
               
               modificar = new JButton("Modificar");
               this.add(modificar);
               modificar.setSize(120,25);
               modificar.setLocation(500,415);
               modificar.addActionListener(this);
               
               activos = new JButton("Ver Activos");
               this.add(activos);
               activos.setSize(110,25);
               activos.setLocation(75,500);
               activos.addActionListener(this);
               activos.setEnabled(false);
               
               espera = new JButton("Ver Espera");
               this.add(espera);
               espera.setSize(110,25);
               espera.setLocation(175,500);
               espera.addActionListener(this);
               
               listaClientesL = new JLabel("Lista de Clientes");
               this.add(listaClientesL);
               listaClientesL.setSize(150,25);
               listaClientesL.setLocation(75,50);
               
               nombreL = new JLabel("Nombre");
               this.add(nombreL);
               nombreL.setSize(75,25);
               nombreL.setLocation(500,75);
               
               apellidoL = new JLabel("Apellido");
               this.add(apellidoL);  
               apellidoL.setSize(75,25);
               apellidoL.setLocation(500,125);
               
               ciL = new JLabel("Cedula");
               this.add(ciL);
               ciL.setSize(75,25);
               ciL.setLocation(500,175);
               
               buscadosL = new JLabel("Destinos Buscados");
               this.add(buscadosL);
               buscadosL.setSize(100,25);
               buscadosL.setLocation(500,275);
               
               realizadosL = new JLabel("Viajes Realizados");
               this.add(realizadosL);
               realizadosL.setSize(100,25);
               realizadosL.setLocation(500,250);
               
               sistema.getEmpresa().addObserver(this);
          }
          
          public void actionPerformed(ActionEvent evento) {
               
               if(evento.getSource().getClass().getName().equals("javax.swing.JButton")){
                    
                    if((evento.getSource() == agregar) || (evento.getSource() == modificar) ){
                         
                         String nombreP = nombre.getText();
                         String apellidoP = apellido.getText();
                         
                         if(nombreP.length() > 0 && apellidoP.length() > 0){
                              
                              try{
                                   int cedulaP = Integer.parseInt(this.ci.getText());
                                   
                                   if(evento.getSource() == agregar){
                                        Cliente cli = new Cliente(nombreP, apellidoP, cedulaP, 0, new ArrayList <Destino>(), new ArrayList <Destino>());
                                        if(!sistema.getEmpresa().agregarCliente(cli)){
                                             JOptionPane.showMessageDialog(null, "ERROR: Ese Cliente ya existe" , "Cliente existente", JOptionPane.ERROR_MESSAGE);
                                        }            
                                   }
                                   else if(evento.getSource() == modificar){
                                        if (!listaClientes.isSelectionEmpty()){
                                             Cliente cli = (Cliente)listaClientes.getSelectedValue();
                                             cli.setNombre(nombreP);
                                             cli.setApellido(apellidoP);
                                             cli.setCedula(cedulaP);
                                        }
                                        else{
                                             JOptionPane.showMessageDialog(null, "No hay cliente seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                   }
                              }
                              catch(NumberFormatException e){
                                   JOptionPane.showMessageDialog(null, "ERROR: Ingrese un numero v�lido en la cedula" , "ERROR", JOptionPane.ERROR_MESSAGE);
                                   this.ci.setText("");
                              }
                         }else{
                              JOptionPane.showMessageDialog(null, "ERROR: Faltan los datos de nombre o apellido" , "ERROR", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                    else if(evento.getSource() == eliminar){
                         if (!listaClientes.isSelectionEmpty()){
                              int respuesta = JOptionPane.showConfirmDialog(null, " �Eliminar este cliente?", "Confirmaci�n", JOptionPane.WARNING_MESSAGE);
                              if (respuesta == JOptionPane.YES_OPTION){
                                   Cliente cli = (Cliente)listaClientes.getSelectedValue();
                                   sistema.getEmpresa().eliminarCliente(cli);
                              }
                         }else{
                              JOptionPane.showMessageDialog(null, "No hay cliente seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                         }
                    }
                    else if(evento.getSource() == activos ){
                         cargarModelo(modeloListaClientes, sistema.getEmpresa().getListaClientes());
                         listaClientes.setModel(modeloListaClientes);
                         cambiarEstadoBotones(true);
                    }
                    else if(evento.getSource() == espera ){
                         cargarModelo(modeloListaEnEspera, sistema.getEmpresa().getListaDeEspera());
                         listaClientes.setModel(modeloListaEnEspera);
                         cambiarEstadoBotones(false);
                    }
               }                             
          }
          
          private void cambiarEstadoBotones(boolean estaHabilitado){
              
               agregar.setEnabled(estaHabilitado);
               eliminar.setEnabled(estaHabilitado);
               modificar.setEnabled(estaHabilitado);
               activos.setEnabled(!estaHabilitado);
               espera.setEnabled(estaHabilitado);//si esta en espera, los demas botones se desactivan
          }
                            
          public void valueChanged(ListSelectionEvent evento) {
               
               if (!listaClientes.isSelectionEmpty()){
                    Cliente cli = (Cliente)listaClientes.getSelectedValue();
                    nombre.setText(cli.getNombre());
                    apellido.setText(cli.getApellido());
                    ci.setText(""+cli.getCedula());
                    cargarModelo(modeloRealizados, cli.getViajesRealizados());
                    cargarModelo(modeloBuscados, cli.getDestinosBuscados());
               }               
          }
          
          private <E> void cargarModelo (DefaultListModel modelo, ArrayList<E> datos){
               
               modelo.clear();
               for(E objeto:datos){
                    if (objeto != null){
                         modelo.addElement(objeto);
                    }
               }     
          }
          
          public void update(Observable o, Object ar){
               
               cargarModelo(modeloListaClientes, sistema.getEmpresa().getListaClientes());
               listaClientes.setSelectedIndex(-1);
               listaClientes.setModel(modeloListaClientes);
          }
     }
     
     @SuppressWarnings("serial")
     private class HandlerTrabajadores extends JPanel implements Observer, ActionListener, ListSelectionListener{
          
          private JList listaTrabajadores;
          /*private JList listaBuscados;
          private JList listaRealizados;*/
          private JTextField nombre;
          private JTextField apellido;
          private JTextField ci;
          private JTextField numeroTrabajador;
          private JTextField ganancias;
          private JButton agregar;
          private JButton eliminar;
          private JButton modificar;
          private JButton sueldo;
          private JButton comision;
          private JLabel listaTrabajadoresL;
          private JLabel nombreL;
          private JLabel apellidoL;
          private JLabel ciL;
          private JLabel numeroTrabajadorL;
          private JLabel gananciasL;
          private JLabel tipoL;
          private JRadioButton botonSueldo;
          private JRadioButton botonComision;
          private ButtonGroup botonGrupo;
          private JPanel radioPanel;
          private DefaultListModel modeloListaTrabajadores;
          private DefaultListModel modeloListaComision;
          /*private DefaultListModel modeloBuscados;
          private DefaultListModel modeloRealizados;*/
          
          public HandlerTrabajadores() {
               
               super();
               this.setSize(900, 750); 
               this.setLayout(null);
               
               modeloListaTrabajadores = new DefaultListModel();
               modeloListaComision = new DefaultListModel();
               
               cargarModelo(modeloListaTrabajadores, sistema.getEmpresa().getListaTrabajadores());
               listaTrabajadores = new JList(modeloListaTrabajadores);
               listaTrabajadores.setSize(200,400);
               listaTrabajadores.setLocation(75,85);
               listaTrabajadores.addListSelectionListener(this);
               listaTrabajadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               this.add(listaTrabajadores);

               nombre = new JTextField();
               this.add(nombre);
               nombre.setSize(200,25);
               nombre.setLocation(500,100);
               
               apellido = new JTextField();
               this.add(apellido);
               apellido.setSize(200,25);
               apellido.setLocation(500,165);
               
               ci = new JTextField();
               this.add(ci);
               ci.setSize(200,25);
               ci.setLocation(500,230);
               
               numeroTrabajador = new JTextField();
               this.add(numeroTrabajador);
               numeroTrabajador.setSize(200,25);
               numeroTrabajador.setLocation(500,295);
               
               ganancias = new JTextField();
               this.add(ganancias);
               ganancias.setSize(200,25);
               ganancias.setLocation(500,360);
               
               agregar = new JButton("Agregar");
               this.add(agregar);
               agregar.setSize(120,25);
               agregar.setLocation(325,250);
               agregar.addActionListener(this);
               
               eliminar = new JButton("Eliminar");
               this.add(eliminar);
               eliminar.setSize(120,25);
               eliminar.setLocation(325,300);
               eliminar.addActionListener(this);
               
               modificar = new JButton("Modificar");
               this.add(modificar);
               modificar.setSize(120,25);
               modificar.setLocation(500, 505);
               modificar.addActionListener(this);
               
               listaTrabajadoresL = new JLabel("Lista de Trabajadores");
               this.add(listaTrabajadoresL);
               listaTrabajadoresL.setSize(150,25);
               listaTrabajadoresL.setLocation(75,50);
               
               nombreL = new JLabel("Nombre");
               this.add(nombreL);
               nombreL.setSize(75,25);
               nombreL.setLocation(500,75);
               
               apellidoL = new JLabel("Apellido");
               this.add(apellidoL);  
               apellidoL.setSize(75,25);
               apellidoL.setLocation(500,140);
               
               ciL = new JLabel("Cedula");
               this.add(ciL);
               ciL.setSize(75,25);
               ciL.setLocation(500, 205);
               
               numeroTrabajadorL = new JLabel("Numero del trabajador");
               this.add(numeroTrabajadorL);
               numeroTrabajadorL.setSize(150,25);
               numeroTrabajadorL.setLocation(500,270);
               
               gananciasL = new JLabel("Ganancias");
               this.add(gananciasL);
               gananciasL.setSize(100,25);
               gananciasL.setLocation(500,335);
               
               tipoL = new JLabel("Tipo de Trabajador");
               this.add(tipoL);
               tipoL.setSize(150, 25);
               tipoL.setLocation(500, 400);
               
               botonSueldo = new JRadioButton("Sueldo fijo", true);
               botonSueldo.setLocation(500, 435);
               botonSueldo.setSize(130, 20);
               botonSueldo.addActionListener(this);
               this.add(botonSueldo);
               botonComision = new JRadioButton ("Por Comision", false);
               botonComision.setLocation(500, 460);
               botonComision.setSize(130, 20);
               botonComision.addActionListener(this);
               this.add(botonComision);
               botonGrupo = new ButtonGroup();               
               botonGrupo.add(botonSueldo);
               botonGrupo.add(botonComision);
               
               sistema.getEmpresa().addObserver(this);
          }
          
          public void actionPerformed(ActionEvent evento) {
               
               if(evento.getSource().getClass().getName().equals("javax.swing.JButton")){
                    
                    if((evento.getSource() == agregar) || (evento.getSource() == modificar) ){
                         
                         String nombreP = nombre.getText();
                         String apellidoP = apellido.getText();
                         
                         if(nombreP.length() > 0 && apellidoP.length() > 0){
                              
                              try{
                                   int cedulaP = Integer.parseInt(this.ci.getText());
                                   int numeroP = Integer.parseInt(this.numeroTrabajador.getText());
                                   double gananciasP = Double.parseDouble(this.ganancias.getText());
                                   
                                   if(evento.getSource() == agregar){
                                        Trabajador trab = new Trabajador();
                                        
                                        if(botonSueldo.isSelected()){                                                                                          
                                             trab = new TrabajadorBase(nombreP, apellidoP, cedulaP, numeroP, gananciasP, new char[0]);
                                        }
                                        else if(botonComision.isSelected()){
                                             trab = new TrabajadorComision(nombreP, apellidoP, cedulaP, numeroP, gananciasP, new char[0]);                                             
                                        }     
                                        
                                        if(!sistema.getEmpresa().agregarTrabajador(trab)){
                                             JOptionPane.showMessageDialog(null, "ERROR: Ese Trabajador ya existe" , "Trabajador existente", JOptionPane.ERROR_MESSAGE);
                                        }         
                                   }
                                   
                                   else if(evento.getSource() == modificar){
                                       
                                        if (!listaTrabajadores.isSelectionEmpty()){
                                             
                                             Trabajador trab = (Trabajador)listaTrabajadores.getSelectedValue();
                                             
                                             if(botonSueldo.isSelected() && trab instanceof TrabajadorComision){                                                  
                                                  sistema.getEmpresa().eliminarTrabajador(trab);     
                                                  trab = new TrabajadorBase();
                                                  sistema.getEmpresa().agregarTrabajador(trab);
                                             }
                                             else if (botonComision.isSelected() && trab instanceof TrabajadorBase){                                                  
                                                  sistema.getEmpresa().eliminarTrabajador(trab);       
                                                  trab = new TrabajadorComision();
                                                  sistema.getEmpresa().agregarTrabajador(trab);
                                             }                                             
                                             
                                             trab.setNombre(nombreP);
                                             trab.setApellido(apellidoP);
                                             trab.setCi(cedulaP);
                                             trab.setNumTrabajador(numeroP);
                                             trab.setGanancias(gananciasP);
                                        }
                                        else{
                                             JOptionPane.showMessageDialog(null, "No hay trabajador seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                   }
                              }
                              catch(NumberFormatException e){
                                   JOptionPane.showMessageDialog(null, "ERROR: Ingrese un numero v�lido en los campos numericos" , "ERROR", JOptionPane.ERROR_MESSAGE);
                                   this.ci.setText("");
                              }    
                         }
                              
                         else{
                              JOptionPane.showMessageDialog(null, "ERROR: Faltan los datos de nombre o apellido " , "ERROR", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                    
                    else if(evento.getSource() == eliminar){
                         if (!listaTrabajadores.isSelectionEmpty()){
                              int respuesta = JOptionPane.showConfirmDialog(null, " �Eliminar este trabajador?", "Confirmaci�n", JOptionPane.WARNING_MESSAGE);
                              if (respuesta == JOptionPane.YES_OPTION){
                                   Trabajador trab = (Trabajador)listaTrabajadores.getSelectedValue();
                                   sistema.getEmpresa().eliminarTrabajador(trab);
                              }
                         }else{
                              JOptionPane.showMessageDialog(null, "No hay trabajador seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                         }
                    }
               }                           
          }
          
          private void cambiarEstadoBotones(boolean estaHabilitado){

               sueldo.setEnabled(!estaHabilitado);
               comision.setEnabled(estaHabilitado);//si esta en espera, los demas botones se desactivan
          }
                              
          public void valueChanged(ListSelectionEvent evento) {
               
               if (!listaTrabajadores.isSelectionEmpty()){
                    Trabajador trab = (Trabajador)listaTrabajadores.getSelectedValue();
                    nombre.setText(trab.getNombre());
                    apellido.setText(trab.getApellido());
                    ci.setText(""+trab.getCi());
                    numeroTrabajador.setText(""+trab.getNumTrabajador());
                    ganancias.setText(""+trab.getGanancias());
                    if(trab instanceof TrabajadorComision){
                         botonComision.setSelected(true);
                    }
                    else if(trab instanceof TrabajadorBase){
                         botonSueldo.setSelected(true);
                    }
               }             
          }
          
          private <E> void cargarModelo (DefaultListModel modelo, ArrayList<E> datos){
               
               modelo.clear();
               for(E objeto:datos){
                    if (objeto != null){
                         modelo.addElement(objeto);
                    }
               } 
          }
          
          public void update(Observable o, Object ar){
               
               cargarModelo(modeloListaTrabajadores, sistema.getEmpresa().getListaTrabajadores());
               listaTrabajadores.setSelectedIndex(-1);
               listaTrabajadores.setModel(modeloListaTrabajadores);
          }
     }
     
     @SuppressWarnings("serial")
     private class HandlerViajes extends JPanel implements Observer, ActionListener, ListSelectionListener{
                    
          private JList listaDestinos;
          private JTextField nombre;
          private JTextField localidad;
          private JTextField pais;
          private JButton agregar;
          private JButton eliminar;
          private JButton modificar;
          private JLabel listaDestinosL;
          private JLabel nombreL;
          private JLabel localidadL;
          private JLabel paisL;
          private JLabel tipoDestinoL;
          private JRadioButton botonCiudad;
          private JRadioButton botonPueblo;
          private JRadioButton botonVilla;
          private JRadioButton botonBalneario;
          private JRadioButton botonNaturaleza;
          private ButtonGroup botonGrupo;
          private JPanel radioPanel;
          private DefaultListModel modeloListaDestinos;
          
          public HandlerViajes() {
               
               super();
               this.setSize(900, 750); 
               this.setLayout(null);
               
               modeloListaDestinos = new DefaultListModel();
               
               cargarModelo(modeloListaDestinos, sistema.getEmpresa().getListaDestinos());
               listaDestinos = new JList(modeloListaDestinos);
               listaDestinos.setSize(200,400);
               listaDestinos.setLocation(75,85);
               listaDestinos.addListSelectionListener(this);
               listaDestinos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               this.add(listaDestinos);

               nombre = new JTextField();
               this.add(nombre);
               nombre.setSize(200,25);
               nombre.setLocation(500,100);
               
               localidad = new JTextField();
               this.add(localidad);
               localidad.setSize(200,25);
               localidad.setLocation(500,165);
               
               pais = new JTextField();
               this.add(pais);
               pais.setSize(200,25);
               pais.setLocation(500,230);
               
               agregar = new JButton("Agregar");
               this.add(agregar);
               agregar.setSize(120,25);
               agregar.setLocation(325,250);
               agregar.addActionListener(this);
               
               eliminar = new JButton("Eliminar");
               this.add(eliminar);
               eliminar.setSize(120,25);
               eliminar.setLocation(325,300);
               eliminar.addActionListener(this);
               
               modificar = new JButton("Modificar");
               this.add(modificar);
               modificar.setSize(120,25);
               modificar.setLocation(500, 445);
               modificar.addActionListener(this);
               
               listaDestinosL = new JLabel("Lista de Destinos");
               this.add(listaDestinosL);
               listaDestinosL.setSize(150,25);
               listaDestinosL.setLocation(75,50);
               
               nombreL = new JLabel("Nombre");
               this.add(nombreL);
               nombreL.setSize(75,25);
               nombreL.setLocation(500,75);
               
               localidadL = new JLabel("Localidad");
               this.add(localidadL);  
               localidadL.setSize(75,25);
               localidadL.setLocation(500,140);
               
               paisL = new JLabel("Pais");
               this.add(paisL);
               paisL.setSize(75,25);
               paisL.setLocation(500, 205);
               
               tipoDestinoL = new JLabel("Tipo");
               this.add(tipoDestinoL);
               tipoDestinoL.setSize(150,25);
               tipoDestinoL.setLocation(500,270);
               
               botonCiudad = new JRadioButton("Ciudad", true);
               botonCiudad.setLocation(500, 305);
               botonCiudad.setSize(130, 20);
               botonCiudad.addActionListener(this);
               this.add(botonCiudad);
               botonPueblo = new JRadioButton ("Pueblo", false);
               botonPueblo.setLocation(500, 330);
               botonPueblo.setSize(130, 20);
               botonPueblo.addActionListener(this);
               this.add(botonPueblo);
               botonVilla = new JRadioButton ("Villa", false);
               botonVilla.setLocation(500, 355);
               botonVilla.setSize(130, 20);
               botonVilla.addActionListener(this);
               this.add(botonVilla);
               botonBalneario = new JRadioButton ("Balneario", false);
               botonBalneario.setLocation(500, 380);
               botonBalneario.setSize(130, 20);
               botonBalneario.addActionListener(this);
               this.add(botonBalneario);
               botonNaturaleza = new JRadioButton ("Naturaleza", false);
               botonNaturaleza.setLocation(500, 405);
               botonNaturaleza.setSize(130, 20);
               botonNaturaleza.addActionListener(this);
               this.add(botonNaturaleza);                              
               botonGrupo = new ButtonGroup();               
               botonGrupo.add(botonCiudad);
               botonGrupo.add(botonPueblo);
               botonGrupo.add(botonVilla);
               botonGrupo.add(botonBalneario);
               botonGrupo.add(botonNaturaleza);
               
               sistema.getEmpresa().addObserver(this);
          }
          
          public void actionPerformed(ActionEvent evento) {
               
               if(evento.getSource().getClass().getName().equals("javax.swing.JButton")){
                    
                    if((evento.getSource() == agregar) || (evento.getSource() == modificar) ){
                         
                         String nombreP = nombre.getText();
                         String localidadP = localidad.getText();
                         String paisP = pais.getText();
                         //String tipoP = tipo.getText();
                         
                         if(nombreP.length() > 0 && localidadP.length() > 0 && paisP.length() >0){
                              
                              /*try{
                                   int cedulaP = Integer.parseInt(this.ci.getText());
                                   int numeroP = Integer.parseInt(this.numeroTrabajador.getText());
                                   double gananciasP = Double.parseDouble(this.ganancias.getText());*/
                                   
                                   if(evento.getSource() == agregar){
                                        Destino dest = new Destino();

                                        if(!sistema.getEmpresa().agregarDestino(dest)){
                                             JOptionPane.showMessageDialog(null, "ERROR: Ese Destino ya existe" , "Destino existente", JOptionPane.ERROR_MESSAGE);
                                        }         
                                   }
                                   
                                   else if(evento.getSource() == modificar){
                                       
                                        if (!listaDestinos.isSelectionEmpty()){
                                             
                                             Destino dest = (Destino)listaDestinos.getSelectedValue();                                          
                                             
                                             dest.setNombre(nombreP);
                                             dest.setLocalidad(localidadP);
                                             dest.setPais(paisP);
                                             //dest.setTipo(tipoP);
                                        }
                                        else{
                                             JOptionPane.showMessageDialog(null, "No hay destino seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                   }
                              //}
                              /*catch(NumberFormatException e){
                                   JOptionPane.showMessageDialog(null, "ERROR: Ingrese un numero v�lido en los campos numericos" , "ERROR", JOptionPane.ERROR_MESSAGE);
                                   this.ci.setText("");
                              }*/    
                         }
                              
                         else{
                              JOptionPane.showMessageDialog(null, "ERROR: Faltan los datos de nombre, localidad o pais" , "ERROR", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                    
                    else if(evento.getSource() == eliminar){
                         if (!listaDestinos.isSelectionEmpty()){
                              int respuesta = JOptionPane.showConfirmDialog(null, " �Eliminar este destino?", "Confirmaci�n", JOptionPane.WARNING_MESSAGE);
                              if (respuesta == JOptionPane.YES_OPTION){
                                   Destino dest = (Destino)listaDestinos.getSelectedValue();
                                   sistema.getEmpresa().eliminarDestino(dest);
                              }
                         }else{
                              JOptionPane.showMessageDialog(null, "No hay destino seleccionado" , "Atenci�n", JOptionPane.INFORMATION_MESSAGE);
                         }
                    }
               }                           
          }
          
          public void valueChanged(ListSelectionEvent evento) {
               
               if (!listaDestinos.isSelectionEmpty()){
                    Destino dest = (Destino)listaDestinos.getSelectedValue();
                    nombre.setText(dest.getNombre());
                    localidad.setText(dest.getLocalidad());
                    pais.setText(dest.getPais());
                    //tipo.setText(dest.getTipo());
                    /*if(trab instanceof TrabajadorComision){
                         botonComision.setSelected(true);
                    }
                    else if(trab instanceof TrabajadorBase){
                         botonSueldo.setSelected(true);
                    }*/
               }             
          }
          
          private <E> void cargarModelo (DefaultListModel modelo, ArrayList<E> datos){
               
               modelo.clear();
               for(E objeto:datos){
                    if (objeto != null){
                         modelo.addElement(objeto);
                    }
               } 
          }
          
          public void update(Observable o, Object ar){
               
               cargarModelo(modeloListaDestinos, sistema.getEmpresa().getListaDestinos());
               listaDestinos.setSelectedIndex(-1);
               listaDestinos.setModel(modeloListaDestinos);
          }
     }
}
