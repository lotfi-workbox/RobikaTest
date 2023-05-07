package com.saeedlotfi.robikaTest.ui._common

import com.saeedlotfi.robikaTest.ui.screen.posts.UserItem

object FakeUserStore {

    //i used this object just in viewModels
    //So if there was a login process, this user could be selected from there
    //also there is another record that belong this user in data layer in current_user.json
    //you can find it in resources in raw folder
    val currentUser = UserItem(
        name = "saeed lotfi",
        image = "https://i.pravatar.cc/300?img=4",
        id = 4L
    )

}