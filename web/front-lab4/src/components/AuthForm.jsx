import Typography from "@mui/material/Typography";
import PropTypes from 'prop-types';
import Grid from '@mui/material/Grid2';
import Box from "@mui/material/Box";
import { Paper, TextField} from "@mui/material";
import {styled} from '@mui/material/styles';
import {PasswordInput} from "./PasswordInput.jsx";

AuthForm.propTypes = {
    isLogin: PropTypes.bool.isRequired,
};

const MyPaperBox = styled(Paper)(({ elevation, theme}) => ({
    // backgroundColor: '#fff',
    padding: theme.spacing(3),
    textAlign: 'center',
    color: theme.palette.text.secondary,
    elevation: elevation
}));

export function AuthForm(props) {
    const {isLogin} = props;

    return (
        // внешний бокс, чтобы всё выровнять
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            sx={{p: 5}}

        >
            <Typography variant="h3" align="center" gutterBottom>
                {isLogin ? "Вход" : "Регистрация"}
            </Typography>

            <MyPaperBox sx={{width: {xs:'90%', sm:'70%', md:'25%'}}} elevation={4}>
                <form>
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
                                label="Имя пользователя"
                                fullWidth
                            >
                            </TextField>
                        </Grid>
                        <Grid size={{xs: 12, md: 3}} item display="flex" justifyContent="center" alignItems="center">
                            <Typography>Пароль</Typography>
                        </Grid>
                        <Grid item size={{xs: 12, md: 9}}>
                            <PasswordInput ></PasswordInput>
                        </Grid>
                        {isLogin ? (<>
                            <Grid size={{xs: 12, md: 3}} item display="flex" justifyContent="center" alignItems="center">
                                <Typography>Повтор</Typography>
                            </Grid>
                            <Grid item size={{xs: 12, md: 9}}>
                                <PasswordInput ></PasswordInput>
                            </Grid>
                        </>)
                        : null}
                    </Grid>
                </form>
            </MyPaperBox>
        </Box>
    );
}