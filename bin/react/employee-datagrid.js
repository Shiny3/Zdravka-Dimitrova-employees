import * as React from "react";
import {
    DataGrid,
    GridToolbarExport,
    GridToolbarContainer,
} from "@material-ui/data-grid";
 
const columns = [
    { field: "id", headerName: "ID", width: 170 },
    { field: "name", headerName: "NAME", width: 170 },
    { field: "age", headerName: "AGE", width: 170 },
];
 
const rows = [
    { id: 1, name: "Gourav", age: 12 },
    { id: 2, name: "Geek", age: 43 },
    { id: 3, name: "Pranav", age: 41 },
    { id: 4, name: "Abhay", age: 34 },
    { id: 5, name: "Pranav", age: 73 },
    { id: 6, name: "Disha", age: 61 },
    { id: 7, name: "Raghav", age: 72 },
    { id: 8, name: "Amit", age: 24 },
    { id: 9, name: "Anuj", age: 48 },
];
 
function MyExportButton() {
    return (
        <GridToolbarContainer>
            <GridToolbarExport />
        </GridToolbarContainer>
    );
}
 
export default function App() {
    return (
        <div style={{ height: 500, width: "80%" }}>
            <h4>
                How to use export our DataGrid as CSV in
                ReactJS?
            </h4>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={5}
                components={{
                    Toolbar: MyExportButton,
                }}
            />
        </div>
    );
}