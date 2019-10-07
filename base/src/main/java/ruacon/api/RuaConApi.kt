package ruacon.api

import ruacon.entity.RuaConApp

class RuaConApi {

    companion object {
        fun getListApp(callback: ICallback<ArrayList<RuaConApp>>) {
            HttpGetRequest(callback).execute()
        }
    }

    interface ICallback<T> {
        fun onSuccess(t: T)
        fun onError()
    }
}