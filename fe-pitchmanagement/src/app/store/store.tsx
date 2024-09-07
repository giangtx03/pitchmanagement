import { configureStore } from '@reduxjs/toolkit';
import SpinnerSlide from '../reducer/SpinnerSlice';
import UserSlice from '../reducer/UserSlice';

const store = configureStore({
    reducer: {
        spinner: SpinnerSlide,
        user: UserSlice
    },
    middleware: (getDefaultMiddleware) => {
        return getDefaultMiddleware({ serializableCheck: false });
    },
});

export default store;
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;