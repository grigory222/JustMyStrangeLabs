import {FormControl, InputAdornment, InputLabel, OutlinedInput} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import {Visibility, VisibilityOff} from "@mui/icons-material";
//import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {changeShowPassword} from "../storage/ShowPasswordSlice.js";


export function PasswordInput({value, onChange}) {
    //const [showPassword, setShowPassword] = useState(false);
    const showPassword = useSelector(state => state.reducer.showPassword.showPassword);
    const dispatch = useDispatch();

    const handleClickShowPassword = () => dispatch(changeShowPassword());

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleMouseUpPassword = (event) => {
        event.preventDefault();
    };

    return (<>
            <FormControl variant="outlined" fullWidth>
                <InputLabel htmlFor="outlined-adornment-password">Пароль</InputLabel>
                <OutlinedInput
                    id="outlined-adornment-password"
                    required={true}
                    type={showPassword ? 'text' : 'password'}
                    value={value}
                    onChange={onChange}
                    endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                                aria-label={
                                    showPassword ? 'hide the password' : 'display the password'
                                }
                                onClick={handleClickShowPassword}
                                onMouseDown={handleMouseDownPassword}
                                onMouseUp={handleMouseUpPassword}
                                edge="end"
                            >
                                {showPassword ? <VisibilityOff/> : <Visibility/>}
                            </IconButton>
                        </InputAdornment>
                    }
                    label="Password"
                /></FormControl>
    </>)
}