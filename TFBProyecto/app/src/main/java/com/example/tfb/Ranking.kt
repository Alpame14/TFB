package com.example.tfb

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.R

class Ranking : AppCompatActivity() {

    data class RankingEntry(val position: Int, val username: String, val score: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val rankingList = listOf(
            RankingEntry(1, "SkyGamerX", 2000),
            RankingEntry(2, "PixelMaster", 1900),
            RankingEntry(3, "ShadowHunter", 1800),
            RankingEntry(4, "BlazeStorm", 1700),
            RankingEntry(5, "CodeWizard", 1600),
            RankingEntry(6, "LunaTide", 1500),
            RankingEntry(7, "CyberNova", 1400),
            RankingEntry(8, "PhantomRush", 1300),
            RankingEntry(9, "AquaFlare", 1200),
            RankingEntry(10, "InfernoCrush", 1100)
        )

        val rankingContainer = findViewById<LinearLayout>(R.id.llRankingContainer)

        for (entry in rankingList) {
            val row = layoutInflater.inflate(R.layout.ranking_row, rankingContainer, false)

            val ivRank = row.findViewById<ImageView>(R.id.ivRank)
            val rankImageId = resources.getIdentifier(
                "rank_%02d".format(entry.position),
                "drawable",
                packageName
            )
            ivRank.setImageResource(rankImageId)

            val tvUsername = row.findViewById<TextView>(R.id.tvUsername)
            tvUsername.text = entry.username

            val tvScore = row.findViewById<TextView>(R.id.tvScore)
            tvScore.text = entry.score.toString()

            rankingContainer.addView(row)
        }
    }
}
