@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import kotlin.random.Random

@Composable
fun RandomIE(state: State) {
    val index by remember { mutableStateOf(Random.nextInt(0, 4)) }

    when (index) {
        0 -> {
            RandomButton(state = state)
        }
        1 -> {
            RandomCheckbox(state = state)
        }
        2 -> {
            RandomRadioButton(state = state)
        }
        3 -> {
            RandomSwitch(state = state)
        }
        else -> {

        }
    }
}

@Composable
fun RandomBottomBar(state: State) {
    val numOfEntries by remember { mutableStateOf(Random.nextInt(3, 6)) }
    val selectedEntry by remember { mutableStateOf(Random.nextInt(1, numOfEntries + 1)) }
    val visible by remember { mutableStateOf(probToBool(probBottomBar))}
    val iconsAndText by remember { mutableStateOf(probsToIndex(*probsBottomBarIconsAndText)) }

    if(visible) {
        NavigationBar {
            for (i in 1..numOfEntries) {
                RandomNavigationBarItem(i == selectedEntry, 1, state)
            }
        }
    }
}

@Composable
fun RowScope.RandomNavigationBarItem(selected: Boolean, iconsAndText: Int, state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("NavigationBarItem")) }
    val text by remember { mutableStateOf(randomText(1, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            NavigationBarItem(
                selected = false,
                icon = {
                    if(iconsAndText < 2) {
                        RandomIcon(
                            state,
                            id,
                            iconSet.random()
                        )
                    }
                       },
                label = {
                    if(iconsAndText > 0) {
                        Text(
                            text = text,
                            color = Color.White
                        )
                    }
                        },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.White),
                modifier = Modifier.background(Color.White)
            )
        } else {
            NavigationBarItem(
                selected = false,
                icon = {
                    if(iconsAndText < 2) {
                        RandomIcon(
                            state,
                            id,
                            iconSet.random()
                        )
                    }
                },
                label = {
                    if(iconsAndText > 0) {
                        Text(
                            text = text,
                            color = Color.Black
                        )
                    }
                },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.Black)
            )
        }
    } else {
        NavigationBarItem(
            selected = selected,
            icon = {
                if(iconsAndText < 2) {
                    RandomIcon(
                        state,
                        id,
                        iconSet.random()
                    )
                }
            },
            label = {
                if(iconsAndText > 0) {
                    Text(
                        text = text
                    )
                }
            },
            onClick = {},
            modifier = Modifier.onCondition(state.mask && id == state.idOfMaskElement, Modifier.background(Color.White))
        )
    }
}

@Composable
fun RandomFloatingActionButton(state: State) {
    val visible by remember { mutableStateOf(probToBool(probFloatingActionButton)) }

    if(visible) {
        val id by remember { mutableStateOf(state.registry.registerElement("FloatingActionButton")) }
        if(state.mask) {
            if(state.idOfMaskElement == id) {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.White,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            } else {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.Black,
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            }
        } else {
            FloatingActionButton(
                onClick = {}
            ) {
                RandomIcon(
                    state,
                    id,
                    iconSet.random()
                )
            }
        }
    }
}

@Composable
fun RandomTopAppBar(state: State) {
    val visible by remember { mutableStateOf(probToBool(probTopAppBar)) }

    if(visible){
        SmallTopAppBar(
            title = { Text(text = randomText(2 ,7)) },
            navigationIcon = {
                val navIcons = setOf(Icons.Filled.Menu, Icons.Filled.ArrowBack)
                val icon = navIcons.random()
                val className = if(icon.name == "Filled.Menu") "Menu" else "BackButton"

                val id by remember{ mutableStateOf(state.registry.registerElement(className)) }

                IconButton(
                    onClick = {},
                    modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.White) else Modifier)
                ) {
                    RandomIcon(
                        state,
                        id,
                        icon
                    )
                }
            },
            actions = {
                val id by remember{ mutableStateOf(state.registry.registerElement("MoreOptions")) }

                IconButton(
                    onClick = {},
                    modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.White) else Modifier)
                ) {
                    RandomIcon(
                        state,
                        id,
                        Icons.Filled.MoreVert
                    )
                }
            }
        )
    }
}

@Composable
fun RandomButton(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Button")) }
    val buttonIcon by remember { mutableStateOf(probToBool(probButtonIcon)) }
    val buttonType by remember { mutableStateOf(probsToIndex(*probsButtonType)) }
    val text by remember { mutableStateOf(randomText(3 ,7)) }
    val fillAvailableSpace by remember { mutableStateOf(probToBool(probButtonFillAvailableSpace)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            if(buttonType != 2) {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text, color = Color.White)
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            } else {
                TextButton(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.White),
                    onClick = {},
                    content = {
                        Text(text, color = Color.White)
                    }
                )
            }
        } else {
            if(buttonType != 2) {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text, color = Color.Black)
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            } else {
                TextButton(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    onClick = {},
                    content = {
                        Text(text, color = Color.Black)
                    }
                )
            }
        }
    } else {
        when(buttonType) {
            0 -> {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text)
                    },
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            }
            1 -> {
                OutlinedButton(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {

                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text)
                    },
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            }
            else -> {
                TextButton(
                    enabled = true,
                    onClick = {},
                    content = {
                        Text(text)
                    }
                )
            }
        }
    }
}

@Composable
fun RandomCheckbox(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Checkbox")) }
    val checked by remember { mutableStateOf(probToBool(probCheckboxChecked)) }
    val text by remember { mutableStateOf(randomText(4, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Checkbox(
                    checked = true,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.White,
                        disabledIndeterminateColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = text)
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Checkbox(
                    checked = checked,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.Black,
                        disabledIndeterminateColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = text)
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = text)
        }
    }
}

@Composable
fun RandomRadioButton(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("RadioButton")) }
    val selected by remember { mutableStateOf(probToBool(probRadioButtonSelected)) }
    val text by remember { mutableStateOf(randomText(4, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = false,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.White,
                        unselectedColor = Color.White
                    ),
                    modifier = Modifier.background(Color.White)
                )
                Text(text = text)
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = true,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text(text = text)
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = {}
            )
            Text(text = text)
        }
    }
}

@Composable
fun RandomSwitch(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Switch")) }
    val checked by remember { mutableStateOf(probToBool(probSwitchChecked)) }
    val text by remember { mutableStateOf(randomText(4, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Switch(
                checked = checked,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.White
                )
            )
        } else {
            Switch(
                checked = checked,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = Color.Black,
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.Black
                )
            )
        }
    } else {
        Switch(
            checked = checked,
            onCheckedChange = {}
        )
    }
}

@Composable
fun RandomIcon(state: State, id: Int, iconToDisplay: ImageVector, modifier: Modifier = Modifier) {
    if (state.mask) {
        if (id == state.idOfMaskElement) {
            Icon(
                imageVector = iconToDisplay,
                contentDescription = null,
                tint = Color.White,
                modifier = modifier
            )
        } else {
            Icon(
                imageVector = iconToDisplay,
                contentDescription = null,
                tint = Color.Black,
                modifier = modifier
            )
        }
    } else {
        Icon(
            imageVector = iconToDisplay,
            contentDescription = null,
            modifier = modifier
        )
    }
}