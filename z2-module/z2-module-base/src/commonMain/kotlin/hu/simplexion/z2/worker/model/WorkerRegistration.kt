package hu.simplexion.z2.worker.model

import hu.simplexion.z2.schematic.runtime.Schematic

class WorkerRegistration : Schematic<WorkerRegistration>() {

    var uuid by uuid<WorkerRegistration>()

    var  provider by uuid<WorkerProvider>()

    var  name by string() maxLength 100
    var  enabled by boolean()

}