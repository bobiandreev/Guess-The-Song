package com.example.guessthesong

import android.R.id
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_text_view.*
import kotlinx.android.synthetic.main.content_text_view.*


class TextViewActivity : AppCompatActivity() {

    /**
     * Called whenever the user requests to access the full text of a song they've seen already.
     * It sets up the layout with the correct text and the listener for the button which loads the
     * songs video on YouTube.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view)
        setSupportActionBar(toolbar)
        val title = intent.getStringExtra("TITLE")

        var toolbar: CollapsingToolbarLayout = findViewById(R.id.toolbar_layout)
        var id = assets.open("Links/"+ title).bufferedReader().readText()
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()

        /**
         * When clicked the appropriate video is launched from the phones YouTube app as it provides
         * additional functionality.
         * Source: StackOverflow - edited
         */
        fab.setOnClickListener { view ->

            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
                startActivity(appIntent)

        }

        toolbar.title = FileReaderObject.transformToNiceString(title)
        val message = intent.getStringExtra("STRING")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        songText.setText(message)
    }

}