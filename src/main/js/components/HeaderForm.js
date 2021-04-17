const React = require('react');
const client = require('../client');

class HeaderForm extends React.Component{
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(megadId, port, event) {
        let input = {pt: port};

        if (event == 'mouseup') {
            input.m = 1;
        }

        client({
                method: 'GET',
                path: `/server/${megadId}`,
                params: input
            });
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title">Прихожая</h5>
                                <div className="d-grid gap-2">
                                    <button className="btn btn-primary data-switch" type="button" data-megad="megad1" data-port="17"
                                            onMouseDown={() => {this.handleClick("megad1", 17, "mousedown")}}
                                            onMouseUp={() => {this.handleClick("megad1", 17, "mouseup")}}
                                    >Прихожая (входная дверь)
                                    </button>
                                    <button className="btn btn-primary data-switch" type="button" data-megad="megad1"
                                            data-port="18">Прихожая (в кухне)
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm">
                        <div className="d-grid gap-2">
                            <button className="btn btn-primary" type="button">Button</button>
                            <button className="btn btn-primary" type="button">Button</button>
                        </div>
                    </div>
                    <div className="col-sm">
                        <div className="d-grid gap-2">
                            <button className="btn btn-primary" type="button">Button</button>
                            <button className="btn btn-primary" type="button">Button</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

module.exports = HeaderForm