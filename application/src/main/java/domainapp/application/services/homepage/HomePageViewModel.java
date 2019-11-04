/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.application.services.homepage;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;

import domainapp.modules.simple.dom.impl.persona.PersonaRepository;
import domainapp.modules.simple.dom.impl.reservaHabitacion.ReservaHabitacion;
import domainapp.modules.simple.dom.impl.reservaHabitacion.ReservaHabitacionRepository;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "domainapp.application.services.homepage.HomePageViewModel"
)
public class HomePageViewModel {

    public List<ReservaHabitacion> getReservas() {
        return reservaHabitacionRepository.listarReservasDeHabitacionesActivas();
    }

    public String title() {

        List<ReservaHabitacion> lista=new ArrayList<ReservaHabitacion>();

        lista=this.getReservas();

        try{
            if (lista.size()==0) {
                return "Cargue Usuario y Habitacion primero para realizar una reserva";
            } else {
                return "Hay "+lista.size() +" reservas realizadas";
            }
        }
        catch (Exception e){
            return "Se ha producido un error";
        }
    }

    @javax.inject.Inject
    PersonaRepository personaRepository;

    @javax.inject.Inject
    ReservaHabitacionRepository reservaHabitacionRepository;
}
