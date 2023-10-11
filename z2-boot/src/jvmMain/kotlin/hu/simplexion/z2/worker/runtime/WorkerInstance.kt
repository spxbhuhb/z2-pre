package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.worker.model.BackgroundWorker
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class WorkerInstance(
    val worker: BackgroundWorker
) {
    companion object {
        val mutex = Mutex()
    }

    private var _job: Job? = null

    suspend fun hasJob() : Boolean {
        mutex.withLock {
            return _job != null
        }
    }

    suspend fun getJob(): Job {
        mutex.withLock {
            return checkNotNull(_job)
        }
    }

    suspend fun setJob(value: Job) {
        mutex.withLock {
            _job = value
        }
    }

}