package com.example.controller;


import com.example.jpa.entities.Employee;
import com.example.jpa.sessions.EmployeeFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "employeeControler")
@SessionScoped
public class EmployeeControler implements Serializable {

    private Employee empleadoActual;
    private List<Employee> listaEmpleados = null;
    @EJB
    private  EmployeeFacade employeeFacade;

    public EmployeeControler() {
        
    }

    public EmployeeFacade getEmployeeFacade() {
        return employeeFacade;
    }

    public Employee getEmpleadoActual() {
        if (empleadoActual == null) {
            empleadoActual = new Employee();
        }
        return empleadoActual;
    }

    public void setEmpleadoActual(Employee empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public List<Employee> getListaEmpleados() {

        if (listaEmpleados == null) {
            try {
                listaEmpleados = getEmployeeFacade().findAll();
            
            } catch (Exception e) {
                addErrorMessage("Error closing resource " + e.getClass().getName(), "message: " + e.getMessage());
                
            }
        }
        return listaEmpleados;
    }

    private void recargarLista() {
        listaEmpleados = null;
    }

    public String prepareCreate() {
        empleadoActual = new Employee();
        return "Create";
    }

    public String prepareList() {
        recargarLista();
        return "/employee/List";
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String prepareView() {
        return "View";
    }

    public String addEmployee() {
        try  {
            getEmployeeFacade().create(empleadoActual);
            addSuccessMessage("Crear Empleado", "Empleado Creado Exitosamente");
            recargarLista();
            return "View";
        
        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            return null;
        }
    }
    public String updateEmployee() {
        try {
           getEmployeeFacade().edit(empleadoActual);
            addSuccessMessage("Actualizar Empleado", "Empleado Actualizado Exitosamente");
            return "View";
        
        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            return null;
        }
    }
    public String deleteEmployee() {
        try {
           getEmployeeFacade().remove(empleadoActual);
            addSuccessMessage("Eliminar Empleado", "Empleado Elimado Exitosamente");
            recargarLista();
  
        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            
        }
        return "List";
    }
    
    
    private void addErrorMessage(String title, String msg){
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);        
    }
     private void addSuccessMessage(String title, String msg){
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, title, msg);
        FacesContext.getCurrentInstance().addMessage("succesInfo", facesMsg);        
    }
}
