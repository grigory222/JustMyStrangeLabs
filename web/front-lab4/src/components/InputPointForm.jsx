import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid2";
import {Autocomplete, TextField} from "@mui/material";
import {useState} from "react";
import Button from "@mui/material/Button";


function sendForm(){

}

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

export function InputPointForm() {
    const [yValue, setYValue] = useState('');

    const handleYChange = (event) => {
        const value = event.target.value;

        // Allow empty input or validate the decimal format
        const regex = /^\d*\.?\d{0,2}$/; // adjust regex to allow empty or valid decimal
        if (regex.test(value)) {
            setYValue(value);
        }
    };

    return (
        <Box
            component='form'
            onSubmit={sendForm()}
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
                    />
                </Grid>
                <Grid item size={3} display='flex' alignItems='center'  justifyContent='center' sx={{mt: 3}}>Y:</Grid>
                <Grid item size={9} display='flex' sx={{mt: 3, pr: 5}}>
                    <TextField
                        value={yValue}
                        label="Координата Y"
                        onChange={handleYChange}
                        fullWidth
                    />
                </Grid>
                <Grid item size={3} display='flex' alignItems='center'  justifyContent='center' sx={{mt: 3}}>R:</Grid>
                <Grid item size={9} display='flex' sx={{mt: 3}}>
                    <Autocomplete
                        disablePortal
                        options={rValues}
                        sx={{ width: '100%' , flexShrink: true, pr: 5}}
                        renderInput={(params) => <TextField {...params} label="Радиус"/>}
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