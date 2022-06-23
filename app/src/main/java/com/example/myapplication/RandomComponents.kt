package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import kotlin.random.Random

@Composable
fun RandomBottomBar(state: State) {
    val numOfEntries by remember { mutableStateOf(Random.nextInt(3, 6)) }
    val selectedEntry by remember { mutableStateOf(Random.nextInt(1, numOfEntries + 1)) }

    if(probToBool(probBottomBar)) {
        NavigationBar {
            for (i in 1..numOfEntries) {
                RandomNavigationBarItem(i == selectedEntry, state)
            }
        }
    }
}

@Composable
fun RowScope.RandomNavigationBarItem(selected: Boolean, state: State) {
    val id by remember{ mutableStateOf(state.registry.registerElement("NavigationBarItem")) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            NavigationBarItem(
                selected = false,
                icon = {
                    Icon(
                        iconSet.random(),
                        contentDescription = "navBarItem",
                        tint = Color.Black
                    )
                },
                /* TODO Randomize label and icon. Also no icon or no label.  */
                label = {
                            Text(
                                text = "test$id" + Random.nextBoolean().toString(),
                                color = Color.Black
                            )
                        },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.Black),
                modifier = Modifier.background(Color.Black)
            )
        } else {
            NavigationBarItem(
                selected = false,
                icon = {
                    Icon(
                        iconSet.random(),
                        contentDescription = "navBarItem",
                        tint = Color.White
                    )
                       },
                /* TODO Randomize label and icon. Also no icon or no label.  */
                label = {
                            Text(
                                text = "test$id" + Random.nextBoolean().toString(),
                                color = Color.White
                            )
                        },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.White)
            )
        }
    } else {
        NavigationBarItem(
            selected = selected,
            icon = {
                Icon(
                    iconSet.random(),
                    contentDescription = "navBarItem"
                )
            },
            /* TODO Randomize label and icon. Also no icon or no label.  */
            label = { Text(text = "test$id" + Random.nextBoolean().toString()) },
            onClick = {},
            modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.Black) else Modifier)
        )
    }
}

@Composable
fun RandomFloatingActionButton(state: State) {
    if(probToBool(probFloatingActionButton)) {
        val id by remember{ mutableStateOf(state.registry.registerElement("FloatingActionButton")) }
        if(state.mask) {
            if(state.idOfMaskElement == id) {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.Black,
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            } else {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.White,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            }
        } else {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(iconSet.random(), contentDescription = "FAB")
            }
        }
    }
}

@Composable
fun RandomTopAppBar(state: State) {
    if(probToBool(probTopAppBar)){
        SmallTopAppBar(
            title = { Text(text = "test") },
            navigationIcon = {
                val navIcons = setOf(Icons.Filled.Menu, Icons.Filled.ArrowBack)
                val icon = navIcons.random()

                val id by remember{ mutableStateOf(state.registry.registerElement(icon.name)) }

                IconButton(
                    onClick = {},
                    modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.Black) else Modifier)
                ) {
                    if (state.mask) {
                        if(state.mask && id == state.idOfMaskElement) {
                            Icon(icon, contentDescription = "navIcon", tint = Color.Black)
                        } else {
                            Icon(icon, contentDescription = "navIcon", tint = Color.White)
                        }
                    } else {
                        Icon(icon, contentDescription = "navIcon")
                    }
                }
            },
            actions = {
                val id by remember{ mutableStateOf(state.registry.registerElement("MoreOptions")) }

                IconButton(
                    onClick = {},
                    modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.Black) else Modifier)
                ) {
                    if (state.mask) {
                        if (state.mask && id == state.idOfMaskElement) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "MoreVert",
                                tint = Color.Black
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "MoreVert",
                                tint = Color.White
                            )
                        }
                    } else {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "MoreVert")
                    }
                }
            }
        )
    }
}
@Composable
fun RandomButton(state: State) {
    val id by remember{ mutableStateOf(state.registry.registerElement("Button")) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Button(
                enabled = true,
                onClick = {},
                content = {
                    Text(Random.nextBoolean().toString(), color = Color.Black)
                },
                colors = ButtonDefaults.buttonColors(Color.Black)
            )
        } else {
            Button(
                enabled = true,
                onClick = {},
                content = {
                    Text(Random.nextBoolean().toString(), color = Color.White)
                },
                colors = ButtonDefaults.buttonColors(Color.White)
            )
        }
    } else {
        Button(
            enabled = Random.nextBoolean(),
            onClick = {},
            content = {
                Text(Random.nextBoolean().toString())
            }
        )
    }

    //TODO add following
    /*
    Icon(
        Icons.Filled.Favorite,
        contentDescription = "Favorite",
        modifier = Modifier.size(ButtonDefaults.IconSize)
    )
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    Text("Like")
*/
}