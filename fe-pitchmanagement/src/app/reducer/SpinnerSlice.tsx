import { createSlice } from "@reduxjs/toolkit"

const initialState = {
    loading : false,
}

const spinnerSlice = createSlice({
    name: "spinner",
    initialState,
    reducers: {
        showOrHideSpinner: (state, action) => {
            state.loading = action.payload;
        }
    }
})

export default spinnerSlice.reducer;
export const {showOrHideSpinner} = spinnerSlice.actions;