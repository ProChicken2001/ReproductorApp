package com.sv.edu.ufg.fis.amb.reproductorapp

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sv.edu.ufg.fis.amb.reproductorapp.pages.MainPage
import com.sv.edu.ufg.fis.amb.reproductorapp.pages.VideoListPage
import com.sv.edu.ufg.fis.amb.reproductorapp.pages.VideoPlayerPage
import com.sv.edu.ufg.fis.amb.reproductorapp.pages.VideoRecorderPage
import com.sv.edu.ufg.fis.amb.reproductorapp.routes.Routes
import com.sv.edu.ufg.fis.amb.reproductorapp.viewModels.MainViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.mainpage.route
    ){
        composable(
            route = Routes.mainpage.route
        ){
            MainPage(
                onRecordVideo = { navController.navigate("record") },
                onViewVideos = { navController.navigate("videos") },
                onPlayLastVideo = {
                    val lastVideo = viewModel.getLastVideo()
                    if (lastVideo != null) {
                        navController.navigate("player?uri=${lastVideo.uri}")
                    }
                }
            )
        }
        composable(
            route = Routes.recordpage.route
        ) {
            VideoRecorderPage(
                onVideoRecorded = { uri ->
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.videospage.route
        ) {
            VideoListPage(
                onVideoSelected = { videoFile ->
                    navController.navigate("player?uri=${videoFile.uri}")
                },
                viewModel = viewModel
            )
        }
        composable(
            route = Routes.playerpage.route,
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            if (uriString != null) {
                VideoPlayerPage(videoUri = Uri.parse(uriString))
            }
        }
    }
}
