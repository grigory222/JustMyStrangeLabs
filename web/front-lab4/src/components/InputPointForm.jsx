
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid2";
import {Autocomplete, TextField} from "@mui/material";
import Button from "@mui/material/Button";
import PropTypes from "prop-types";
import {useState} from "react";

const xValues = [
    {label: -3},
    {label: -2},
    {label: -1},
    {label:  0},
    {label:  1},
    {label:  2},
    {label:  3},
    {label:  4},
    {label:  5},
];

const rValues = [
    {label: -3},
    {label: -2},
    {label: -1},
    {label:  0},
    {label:  1},
    {label:  2},
    {label:  3},
    {label:  4},
    {label:  5},
];

export function InputPointForm({formData, setFormData, formSubmitHandler, errorMessage}) {


    const handleYChange = (event) => {
        const value = event.target.value;
        const regex = /^-?\d*\.?\d{0,2}$/;
        if (regex.test(value)) {
            setFormData((prev) => ({ ...prev, y: value }));
        }
    };

    return (
        <Box
            component='form'
            onSubmit={formSubmitHandler}
        >
            <Grid container sx={{fontFamily: 'Arial'}}>
                <Grid item size={12} fullWidth><Typography>Введите данные</Typography></Grid>
                <Grid item size={3} display='flex' alignItems='center'  justifyContent='center' sx={{mt: 3}}>X:</Grid>
                <Grid item size={9} display='flex' sx={{mt: 3}}>
                    <Autocomplete
                        disablePortal
                        options={xValues}
                        sx={{ width: '100%' , flexShrink: true, pr: 5}}
                        renderInput={(params) => <TextField {...params} label="Координата X"/>}
                        onChange={(event, newValue) => setFormData((prev) => ({ ...prev, x: newValue?.label ?? null }))}
                    />
                </Grid>
                <Grid item size={3} display='flex' alignItems='center'  justifyContent='center' sx={{mt: 3}}>Y:</Grid>
                <Grid item size={9} display='flex' sx={{mt: 3, pr: 5}}>
                    <TextField
                        value={formData.y}
                        label="Координата Y"
                        onChange={handleYChange}
                        fullWidth
                        error={!!errorMessage} // Показывает ошибку
                        helperText={errorMessage} // Сообщение об ошибке
                    />
                </Grid>
                <Grid item size={3} display='flex' alignItems='center'  justifyContent='center' sx={{mt: 3}}>R:</Grid>
                <Grid item size={9} display='flex' sx={{mt: 3}}>
                    <Autocomplete
                        disablePortal
                        options={rValues}
                        sx={{ width: '100%' , flexShrink: true, pr: 5}}
                        renderInput={(params) => <TextField {...params} label="Радиус"/>}
                        onChange={(event, newValue) => setFormData((prev) => ({ ...prev, r: newValue?.label ?? null }))}
                    />
                </Grid>
                <Grid item size={12} display='flex' justifyContent='right' sx={{mt:3, mx:5}}>
                    <Button type="submit" variant="contained" sx={{fontSize: 12, width:'50%' }}>
                        Отправить
                    </Button>
                </Grid>
            </Grid>

        </Box>
    );
}

// Добавьте prop-types для проверки типов
InputPointForm.propTypes = {
    formData: PropTypes.shape({
        x: PropTypes.number,
        y: PropTypes.string,
        r: PropTypes.number,
    }).isRequired, // formData должен быть объектом с заданной структурой
    setFormData: PropTypes.func.isRequired, // setFormData — это функция
    formSubmitHandler: PropTypes.func.isRequired, // formSubmitHandler — это функция
    errorMessage: PropTypes.string
};