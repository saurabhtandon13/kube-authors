import React, {Component} from 'react';
import './Authors.css';
import axios from 'axios'


export default class Authors extends Component {

    constructor(props) {
        super(props)
        this.state = {
            selectedAuthor: 1,
            enableSocial : false
            //enableSocial : true
          }
      
    }

    componentWillMount(){
        console.log('First this called');
      }
    

      
    componentDidMount() {
        console.log("hello mounting");
        this.getAuthors();
    }

    getAuthors() {
        console.log("hello123");
        console.log(process.env.PUBLIC_URL);
        axios.get('assets/samplejson/authors.json').then(response => {
          this.setState({authors: response})
        })
    }
    

    render() {
        console.log("hello rendering");
        if (!this.state.authors)
            return (<p>Loading data</p>)
        
        return (
            <section className="container py-5">
                <h2 className="h4 block-title text-center mt-2">This month top authors</h2>
                <div className="row pt-3">
                {
                    this.state.authors.data.map(item => 

                        <div className="col-lg-3 col-sm-6 mb-30 pb-2">
                            <div className="team-card-style-3 mx-auto">
                                <div className="team-thumb">
                                    <img src={item.photo} alt="Author Picture"/>
                                </div>

                                <h4 className="team-name">{item.name}</h4>
                                <a className="team-contact-link" href="tel:+72056557984">
                                    <i className="fe-icon-phone"></i>&nbsp;{item.phone}
                                </a>
                                <a className="team-contact-link" href="mailto:info@example.com">
                                    <i className="fe-icon-mail"></i>&nbsp;{item.email}
                                </a>


                                <div className={"team-social-bar-wrap " +  (this.state.enableSocial ? 'visible' : 'invisible')}>
                                    <div class="team-social-bar">
                                        <a class="social-btn sb-style-1 sb-twitter" href="#">
                                            <i class="fa fa-twitter"></i> 
                                        </a> 
                                        <a class="social-btn sb-style-1 sb-github" href="#"> 
                                            <i class="fa fa-github"></i> 
                                        </a> 
                                        <a class="social-btn sb-style-1 sb-stackoverflow" href="#"> 
                                            <i class="fa fa-linkedin"></i> 
                                        </a> 
                                        <a class="social-btn sb-style-1 sb-skype" href="#"> 
                                            <i class="fa fa-skype"></i> 
                                        </a>
                                    </div>
                                </div>
                            </div>

                            

                            

                            
                        </div>
                    )
                }
                </div>
            </section>


        )    
    
    
    }


    
    




}