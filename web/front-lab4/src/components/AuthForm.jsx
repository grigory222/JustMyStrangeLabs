import Typography from "@mui/material/Typography";
import PropTypes from 'prop-types';
import Grid from '@mui/material/Grid2';
import Box from "@mui/material/Box";

AuthForm.propTypes = {
    isLogin: PropTypes.bool.isRequired,
};

export function AuthForm(props) {
    const {isLogin} = props;

    return (
        <>
            <Typography variant="h3">
                {isLogin ? "Вход" : "Регистрация"}
            </Typography>
            <form>
                <Box sx={{
                    flexGrow: 0.7,
                    border: '1px solid grey',
                    borderRadius: 5,
                    bgcolor : 'lightgrey',
                    
                }}>
                    <Grid container spacing={2}>
                        <Grid xs={12}>asdas</Grid>
                    </Grid>
                </Box>
            </form>
        </>
    );
}