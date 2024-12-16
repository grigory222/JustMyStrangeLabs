import Typography from "@mui/material/Typography";
import PropTypes from 'prop-types';
import Grid from '@mui/material/Grid2';
import Box from "@mui/material/Box";
import {Alert, Paper, TextField, Tooltip} from "@mui/material";
import {styled} from '@mui/material/styles';
import {PasswordInput} from "./PasswordInput.jsx";
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {useLoginMutation} from "../api/myLegendaryApi.js";
import {setLoggedIn} from "../storage/IsLoggedSlice.js";

AuthForm.propTypes = {
    isLogin: PropTypes.bool.isRequired,
};

export const MyPaperBox = styled(Paper)(({elevation, theme}) => ({
    // backgroundColor: '#fff',
    padding: theme.spacing(3),
    textAlign: 'center',
    color: theme.palette.text.secondary,
    elevation: elevation,
}));

export function AuthForm(props) {
    const {isLogin} = props;
    const navigate = useNavigate();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const dispatch = useDispatch();
    const [login] = useLoginMutation();
    const [alert, setAlert] = useState(false);
    const [alertContent, setAlertContent] = useState('');
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const formSubmitHandler = async (e) => {
        e.preventDefault();

        if (isSubmitting) return; // Предотвращаем повторное нажатие
        setIsSubmitting(true);

        try {
            if (isLogin) {
                const payload = await login({username: formData.username, password: formData.password}).unwrap()
                console.log("payload", payload)
                dispatch(setLoggedIn(true));
            }
        } catch (error) {
            setAlert(true);
            setAlertContent(error.data.error);
            console.log(error.data.error);
        }
        setIsSubmitting(false);
    }

    return (
        // внешний бокс, чтобы всё выровнять
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            sx={{p: 5}}

        >
            <Typography variant="h4" align="center" gutterBottom>
                {isLogin ? "Вход" : "Регистрация"}
            </Typography>

            <MyPaperBox
                sx={{width: {xs: '90%', sm: '70%', md: '25%'}}}
                elevation={4}
                component="form"
                onSubmit={formSubmitHandler}>

                <Grid
                    container
                    columnSpacing={2}
                    rowSpacing={2}
                >
                    <Grid size={{xs: 12, md: 3}} item display="flex" justifyContent="center" alignItems="center">
                        <Typography>Логин</Typography>
                    </Grid>
                    <Grid item size={{xs: 12, md: 9}}>
                        <TextField
                            id="outlined-required"
                            required
                            label="Имя пользователя"
                            fullWidth
                            value={formData.username}
                            onChange={(e) => setFormData({...formData, username: e.target.value})}
                        >
                        </TextField>
                    </Grid>
                    <Grid size={{xs: 12, md: 3}} item display="flex" justifyContent="center" alignItems="center">
                        <Typography>Пароль</Typography>
                    </Grid>
                    <Grid item size={{xs: 12, md: 9}}>
                        <PasswordInput
                            value={formData.password}
                            onChange={(e) => setFormData({...formData, password: e.target.value})}
                        ></PasswordInput>
                    </Grid>
                    {!isLogin ? (<>
                            <Grid size={{xs: 12, md: 3}} item display="flex" justifyContent="center" alignItems="center">
                                <Typography>Повтор</Typography>
                            </Grid>
                            <Grid item size={{xs: 12, md: 9}}>
                                <PasswordInput></PasswordInput>
                            </Grid>
                        </>)
                        : null}

                    <Grid item offset={{md: 4}} size={{xs: 12, md: 4}} sx={{order: {xs: 2, md: 1}}}>
                        <Tooltip
                            title={isLogin ? "Нажмите, чтобы перейти к регистрации" : "Нажмите, чтобы войти в аккаунт"}
                            arrow>
                            <Typography sx={{
                                fontSize: 9,
                                p: 1,
                                textDecoration: 'none', // Убираем подчеркивание по умолчанию
                                "&:hover": {
                                    textDecoration: 'underline', // Подчеркивание при наведении
                                    cursor: 'pointer', // Курсор при наведении
                                }
                            }}
                                        onClick={() =>
                                            isLogin ? navigate('/register') : navigate('/login')
                                        }
                            >
                                {isLogin ? "Ещё нет аккаунта?" : "Уже есть аккаунт?"}
                            </Typography>
                        </Tooltip>
                    </Grid>
                    <Grid item size={{xs: 12, md: 4}} sx={{order: {xs: 1, md: 2}}}>
                        <Button type="submit" variant="contained" fullWidth sx={{fontSize: 12}}>
                            {isLogin ? "Вход" : "Регистрация"}
                        </Button>
                    </Grid>

                </Grid>
                {alert ? <Alert severity='error' sx={{mt: 2}}>{alertContent}</Alert> : <></>}
            </MyPaperBox>
        </Box>
    );
}