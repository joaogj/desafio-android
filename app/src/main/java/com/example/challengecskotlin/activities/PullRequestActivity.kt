package com.example.challengecskotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengecskotlin.R
import com.example.challengecskotlin.adapters.PullRequestAdapter
import com.example.challengecskotlin.api.GithubApi
import com.example.challengecskotlin.models.PullRequestObject
import kotlinx.android.synthetic.main.activity_pull_request.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PullRequestActivity : AppCompatActivity() {

    private lateinit var loadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_request)
        setSupportActionBar(findViewById(R.id.my_toolbar)) //identificando action bar

        loadingBar = findViewById(R.id.pr_progress)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //habilitando botão de retorno na action bar
        getIncomingIntent()
    }

    //função que recebe os parâmetros passados da requisição de repositorios para montar a requisição
    //de pull requests e título da action bar
    private fun getIncomingIntent(){
        val PARAM_LOGIN = "login"
        val PARAM_REPOSITORY = "repositoryName"

        if(intent.hasExtra(PARAM_LOGIN) && intent.hasExtra(PARAM_REPOSITORY)){
            val login: String = intent.getStringExtra(PARAM_LOGIN)
            val repositoryName: String = intent.getStringExtra(PARAM_REPOSITORY)
            getPullRequests(login, repositoryName)
            title = repositoryName //atribuindo o título da action bar
        }
    }

    private fun getPullRequests(login: String, name: String){
        GithubApi.searchService.fetchPullRequests(login, name).enqueue(object : Callback<List<PullRequestObject>> {
            override fun onResponse(call: Call<List<PullRequestObject>>, response: Response<List<PullRequestObject>>) {
                if(response.isSuccessful) {
                    showData(response.body()!!)

                    //tratamento de resposta vazia
                    if(response.body()!!.isEmpty()){
                        Toast.makeText(this@PullRequestActivity, "Não há pull requests nesse repositório", Toast.LENGTH_LONG).show()
                    }
                }
                loadingBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<PullRequestObject>>, t: Throwable) {
                loadingBar.visibility = View.GONE
                Toast.makeText(this@PullRequestActivity, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    //populando recycler view
    private fun showData(pullRequests: List<PullRequestObject>) {
        rv_pull_requests.apply{
            layoutManager = LinearLayoutManager(this@PullRequestActivity)
            adapter = PullRequestAdapter(pullRequests, this@PullRequestActivity)

            val dividerItemDecoration = DividerItemDecoration(
                rv_pull_requests.context,
                (layoutManager as LinearLayoutManager).orientation
            )
            rv_pull_requests.addItemDecoration(dividerItemDecoration)
        }
    }
}
