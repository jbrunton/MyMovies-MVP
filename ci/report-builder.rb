#!/usr/bin/env ruby

require 'nokogiri'
require 'byebug'

artifacts_dir = File.join(Dir.pwd, ARGV.first)

puts "Scanning #{artifacts_dir}"

builder = Nokogiri::HTML::Builder.new do |doc|
  doc.html {
    doc.head {
      doc.link(:href=>"https://fonts.googleapis.com/icon?family=Material+Icons", :rel=>"stylesheet")
      doc.link(:href=>"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css", :rel=>"stylesheet")
      doc.meta(:name=>"viewport", :content=>"width=device-width, initial-scale=1.0")
    }
    doc.body {
      doc.script(:type=>"text/javascript", :src=>"https://code.jquery.com/jquery-3.2.1.min.js")
      doc.script(:type=>"text/javascript", :src=>"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js")

      doc.nav {
        doc.div(:class => "nav-wrapper container") {
          doc.a(:href => "#", :class => "brand-logo") {
            doc.text "Test Results"
          }
        }
      }
      doc.div(:class => "container") {
          Dir.glob(artifacts_dir + '**/*.xml').each do |f|
            puts "Found #{f}"

            device = f.split(File::SEPARATOR)[-2]
            doc.h4(device)

            xml_report = Nokogiri::XML(File.open(f))

            doc.ul(:class=>"collapsible", 'data-collapsible'=>"accordion") {
              xml_report.xpath('//testcase').each do |testcase|
                failed = testcase.xpath('failure').any?
                test_name = testcase.attributes['name'].value
                class_name = testcase.attributes['classname'].value
                screens = Dir.glob(File.dirname(f) + "/artifacts/#{class_name}-*.jpg")
                doc.li {
                  doc.div(:class => "collapsible-header") {
                    icon_color = failed ? "red" : "green"
                    doc.i(:class => "material-icons #{icon_color}-text") {
                      doc.text "check"
                    }
                    doc.text test_name
                  }
                  doc.div(:class => "collapsible-body") {
                    doc.div {
                      (screens + screens).each do |screen|
                        doc.div(:class => "card", :style => "display: inline-block; width: 200px") {
                          doc.div(:class => "card-image", :style => "width: 200px") {
                            doc.img(:src => screen)
                          }
                        }
                      end
                    }
                    if failed
                      failure_text = testcase.xpath('failure').first.text
                      doc.pre failure_text
                    end
                  }
                }
              end
            }
          end
      }
    }
  }
end

File.write('report.html', builder.to_html)
