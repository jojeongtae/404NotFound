import { createSlice } from "@reduxjs/toolkit";

const boardSlice = createSlice({
    name: "board",
    initialState: {
        author: null,
        title: null,
        body: null,
        imgsrc: null,
        id: null,
        views: null,
        category: null,
        recommend:null,
        authorNickname:null,
        userNickname:null,
        grade:null,
<<<<<<< HEAD
=======
        imgsrc:null,
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
    },
    reducers: {
        setPostDetails: (state, action) => {
            Object.assign(state, action.payload);
        },
        clearPost: (state) => {
            state.author = null;
                state.title = null;
                state.body = null;
                state.imgsrc = null;
                state.id = null;
                state.views = null;
                state.category = null;
                state.recommend = null;
                state.authorNickname = null;
                state.userNickname = null;
                state.grade = null;
<<<<<<< HEAD
        },
=======
                state.imgsrc = null;

            },
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780

    },

});
export const { setPostDetails, clearPost } = boardSlice.actions;
export default boardSlice.reducer;
